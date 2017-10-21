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

import android.view.ContextMenu;
import android.view.View;

import uk.org.ngo.squeezer.R;
import uk.org.ngo.squeezer.framework.BaseListActivity;
import uk.org.ngo.squeezer.framework.ItemView;
import uk.org.ngo.squeezer.model.Plugin;

public class RadioView extends PluginView {

    public RadioView(BaseListActivity<Plugin> activity) {
        super(activity);
    }

    @Override
    public String getQuantityString(int quantity) {
        return getActivity().getResources().getQuantityString(R.plurals.radio, quantity);
    }

    @Override
    public void onItemSelected(int index, Plugin item) {
        PluginItemListActivity.show(getActivity(), item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ItemView.ContextMenuInfo menuInfo) {
    }
}
