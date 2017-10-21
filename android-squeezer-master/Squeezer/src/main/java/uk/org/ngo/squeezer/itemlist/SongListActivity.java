/*
 * Copyright (c) 2011 Kurt Aaholst <kaaholst@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.org.ngo.squeezer.itemlist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import uk.org.ngo.squeezer.Preferences;
import uk.org.ngo.squeezer.R;
import uk.org.ngo.squeezer.Util;
import uk.org.ngo.squeezer.framework.BaseListActivity;
import uk.org.ngo.squeezer.framework.Item;
import uk.org.ngo.squeezer.framework.ItemView;
import uk.org.ngo.squeezer.framework.PlaylistItem;
import uk.org.ngo.squeezer.itemlist.GenreSpinner.GenreSpinnerCallback;
import uk.org.ngo.squeezer.itemlist.YearSpinner.YearSpinnerCallback;
import uk.org.ngo.squeezer.itemlist.dialog.SongFilterDialog;
import uk.org.ngo.squeezer.itemlist.dialog.SongViewDialog;
import uk.org.ngo.squeezer.menu.BaseMenuFragment;
import uk.org.ngo.squeezer.menu.FilterMenuFragment;
import uk.org.ngo.squeezer.menu.ViewMenuItemFragment;
import uk.org.ngo.squeezer.model.Album;
import uk.org.ngo.squeezer.model.Artist;
import uk.org.ngo.squeezer.model.Genre;
import uk.org.ngo.squeezer.model.Song;
import uk.org.ngo.squeezer.model.Year;
import uk.org.ngo.squeezer.service.ISqueezeService;
import uk.org.ngo.squeezer.service.event.HandshakeComplete;
import uk.org.ngo.squeezer.util.ImageFetcher;

public class SongListActivity extends BaseListActivity<Song>
        implements GenreSpinnerCallback, YearSpinnerCallback,
        FilterMenuFragment.FilterableListActivity,
        ViewMenuItemFragment.ListActivityWithViewMenu<Song, SongViewDialog.SongListLayout, SongViewDialog.SongsSortOrder> {

    private SongViewDialog.SongsSortOrder sortOrder = SongViewDialog.SongsSortOrder.title;

    private SongViewDialog.SongListLayout listLayout = SongViewDialog.SongListLayout.list;

    private String searchString;

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    private Album album;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    private Artist artist;

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    private Year year;

    @Override
    public Year getYear() {
        return year;
    }

    @Override
    public void setYear(Year year) {
        this.year = year;
    }

    private Genre genre;

    @Override
    public Genre getGenre() {
        return genre;
    }

    @Override
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    private SongView songViewLogic;

    private MenuItem playButton;

    private MenuItem addButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setListLayout();
        super.onCreate(savedInstanceState);

        // Set the album header.
        if (album != null) {
            ImageView artwork = (ImageView) findViewById(R.id.album);
            TextView albumView = (TextView) findViewById(R.id.albumname);
            TextView artistView = (TextView) findViewById(R.id.artistname);
            TextView yearView = (TextView) findViewById(R.id.yearname);
            ImageView btnContextMenu = (ImageView) findViewById(R.id.context_menu);

            albumView.setText(album.getName());
            artistView.setText(album.getArtist());
            if (album.getYear() != 0) {
                yearView.setText(Integer.toString(album.getYear()));
            }

            Uri artworkUrl = album.getArtworkUrl();

            if (artworkUrl.equals(Uri.EMPTY)) {
                artwork.setImageResource(R.drawable.icon_album_noart);
            } else {
                ImageFetcher.getInstance(this).loadImage(artworkUrl, artwork);
            }

            btnContextMenu.setOnCreateContextMenuListener(this);

            btnContextMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.showContextMenu();
                }
            });
        } else
        if (artist != null) {
            TextView header = (TextView) findViewById(R.id.header);
            header.setVisibility(View.VISIBLE);
            header.setText(getString(R.string.songs_by_header, artist.getName()));
        }

        // Adapter has been created (or restored from the fragment) by this point,
        // so fetch the itemView that was used.
        songViewLogic = (SongView) getItemAdapter().getItemView();

        BaseMenuFragment.add(this, FilterMenuFragment.class);
        BaseMenuFragment.add(this, ViewMenuItemFragment.class);

        songViewLogic.setBrowseByAlbum(album != null);
        songViewLogic.setBrowseByArtist(artist != null);
    }

    @Override
    protected int getContentView() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                if (Album.class.getName().equals(key)) {
                    album = extras.getParcelable(key);
                    sortOrder = SongViewDialog.SongsSortOrder.tracknum;
                } else if (Artist.class.getName().equals(key)) {
                    artist = extras.getParcelable(key);
                } else if (Year.class.getName().equals(key)) {
                    year = extras.getParcelable(key);
                } else if (Genre.class.getName().equals(key)) {
                    genre = extras.getParcelable(key);
                } else {
                    Log.e(getTag(), "Unexpected extra value: " + key + "("
                            + extras.get(key).getClass().getName() + ")");
                }
            }
        }

        if (album != null) {
            return R.layout.item_list_album;
        }

        return (listLayout == SongViewDialog.SongListLayout.grid) ? R.layout.item_grid
                : super.getContentView();
    }

    /**
     * Updates the artwork in the UI. Can only be called after the server handshake has
     * completed, as the IP port is required to construct the artwork URL.
     */
    private void updateArtwork() {
        // Set artwork that requires a service connection.
        if (album != null) {
            ImageView artwork = (ImageView) findViewById(R.id.album);
            Uri artworkUrl = album.getArtworkUrl();

            if (artworkUrl.equals(Uri.EMPTY)) {
                artwork.setImageResource(R.drawable.icon_album_noart);
            } else {
                ImageFetcher.getInstance(this).loadImage(artworkUrl, artwork);
            }
        }
    }

    /**
     * Ensures that the artwork in the UI is updated after the server handshake completes.
     */
    @Override
    public void onEventMainThread(HandshakeComplete event) {
        super.onEventMainThread(event);
        updateArtwork();
    }

    public static void show(Context context, Item... items) {
        final Intent intent = new Intent(context, SongListActivity.class);
        for (Item item : items) {
            intent.putExtra(item.getClass().getName(), item);
        }
        context.startActivity(intent);
    }

    @Override
    public ItemView<Song> createItemView() {
        if (album != null) {
            songViewLogic = new SongView(this);
            songViewLogic.setDetails(SongView.DETAILS_TRACK_NO |
                    SongView.DETAILS_DURATION |
                    SongView.DETAILS_ARTIST_IF_COMPILATION);
        } else if (artist != null) {
            songViewLogic = songViewLogicFromListLayout();
            songViewLogic.setDetails(SongView.DETAILS_DURATION |
                    SongView.DETAILS_ALBUM |
                    SongView.DETAILS_YEAR);
        } else {
            songViewLogic = songViewLogicFromListLayout();
            songViewLogic.setDetails(SongView.DETAILS_ARTIST |
                    SongView.DETAILS_ALBUM |
                    SongView.DETAILS_YEAR);
        }

        return songViewLogic;
    }

    private SongViewWithArt songViewLogicFromListLayout() {
        return (listLayout == SongViewDialog.SongListLayout.grid) ? new SongGridView(this) : new SongViewWithArt(this);
    }

    @Override
    protected void orderPage(@NonNull ISqueezeService service, int start) {
        service.songs(this, start, sortOrder.name(), searchString, album, artist, year, genre);

        boolean canPlay = (getCurrentPlaylistItem() != null);
        if (playButton != null) {
            playButton.setVisible(canPlay);
        }

        if (addButton != null) {
            addButton.setVisible(canPlay);
        }
    }

    @Override
    public SongViewDialog.SongsSortOrder getSortOrder() {
        return sortOrder;
    }

    @Override
    public void setSortOrder(SongViewDialog.SongsSortOrder sortOrder) {
        this.sortOrder = sortOrder;
        clearAndReOrderItems();
    }

    @Override
    public SongViewDialog.SongListLayout getListLayout() {
        return listLayout;
    }

    /**
     * Set the preferred album list layout.
     */
    private void setListLayout() {
        SharedPreferences preferences = getSharedPreferences(Preferences.NAME, 0);
        String listLayoutString = preferences.getString(Preferences.KEY_SONG_LIST_LAYOUT, null);
        if (listLayoutString != null) {
            listLayout = SongViewDialog.SongListLayout.valueOf(listLayoutString);
        }
    }

    @Override
    public void setListLayout(SongViewDialog.SongListLayout listLayout) {
        SharedPreferences preferences = getSharedPreferences(Preferences.NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Preferences.KEY_SONG_LIST_LAYOUT, listLayout.name());
        editor.commit();

        startActivity(getIntent());
        finish();
    }

    @Override
    public void showFilterDialog() {
        new SongFilterDialog().show(getSupportFragmentManager(), "SongFilterDialog");
    }

    @Override
    public void showViewDialog() {
        new SongViewDialog().show(this.getSupportFragmentManager(), "OrderDialog");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (album != null) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.albumcontextmenu, menu);

            // Hide the option to view the album.
            MenuItem browse = menu.findItem(R.id.browse_songs);
            if (browse != null) {
                browse.setVisible(false);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        // If info is null then this the context menu from the header, not a list item.
        if (info == null) {
            switch (item.getItemId()) {
                case R.id.play_now:
                    play(album);
                    return true;

                case R.id.add_to_playlist:
                    add(album);
                    return true;

                case R.id.browse_artists:
                    ArtistListActivity.show(this, album);
                    return true;

                case R.id.download:
                    downloadItem(album);
                    return true;

                default:
                    throw new IllegalStateException("Unknown menu ID.");
            }

        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only show the play entries from the options menu for albums (the context menu already
        // shows them).
        if (album == null) {
            getMenuInflater().inflate(R.menu.playmenu, menu);
            playButton = menu.findItem(R.id.play_now);
            addButton = menu.findItem(R.id.add_to_playlist);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Sets the enabled state of the R.menu.currentplaylistmenu items.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final boolean boundToService = getService() != null;

        // Note: Seen a crash reported here where playButton is null (on a rooted device).
        if (album == null && playButton != null && addButton != null) {
            playButton.setEnabled(boundToService);
            addButton.setEnabled(boundToService);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        PlaylistItem playlistItem = getCurrentPlaylistItem();
        switch (item.getItemId()) {
            case R.id.play_now:
                if (playlistItem != null) {
                    play(playlistItem);
                }
                return true;
            case R.id.add_to_playlist:
                if (playlistItem != null) {
                    add(playlistItem);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private PlaylistItem getCurrentPlaylistItem() {
        int playlistItems = Util
                .countBooleans(album != null, artist != null, genre != null, year != null);
        if (playlistItems == 1 && TextUtils.isEmpty(searchString)) {
            if (album != null) {
                return album;
            }
            if (artist != null) {
                return artist;
            }
            if (genre != null) {
                return genre;
            }
            if (year != null) {
                return year;
            }
        }
        return null;
    }

}
