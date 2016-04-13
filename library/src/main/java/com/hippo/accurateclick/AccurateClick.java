/*
 * Copyright 2016 Hippo Seven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hippo.accurateclick;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.hippo.hotspot.Hotspot;
import com.hippo.hotspot.Hotspotable;

public final class AccurateClick {
    private AccurateClick() {}

    /**
     * Register a callback to be invoked when this view is clicked.
     *
     * @param view The target view
     * @param listener The callback that will run
     */
    public static void setOnAccurateClickListener(@NonNull View view, @Nullable OnAccurateClickListener listener) {
        if (listener != null) {
            Object obj = view.getTag(R.id.view_tag_accurate_click_helper);
            if (obj instanceof AccurateClickHelper) {
                // Update on accurate click listener
                view.setTag(R.id.view_tag_on_accurate_click_listener, listener);
            } else {
                // Set accurate click helper
                AccurateClickHelper accurateClickHelper = new AccurateClickHelper();
                view.setTag(R.id.view_tag_accurate_click_helper, accurateClickHelper);
                Hotspot.addHotspotable(view, accurateClickHelper);
                // Set on accurate click listener
                view.setTag(R.id.view_tag_on_accurate_click_listener, listener);
                // Set on click listener
                view.setOnClickListener(new DelegateClickListener());
            }
        } else {
            // Remove accurate click helper
            Object obj = view.getTag(R.id.view_tag_accurate_click_helper);
            if (obj instanceof AccurateClickHelper) {
                AccurateClickHelper accurateClickHelper = (AccurateClickHelper) obj;
                Hotspot.removeHotspotable(view, accurateClickHelper);
            }
            view.setTag(R.id.view_tag_accurate_click_helper, null);
            view.setTag(R.id.view_tag_on_accurate_click_listener, null);
            // Remove on click listener
            view.setOnClickListener(null);
        }
    }

    private static class DelegateClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Object accurateClickHelper = v.getTag(R.id.view_tag_accurate_click_helper);
            Object listener = v.getTag(R.id.view_tag_on_accurate_click_listener);
            if (accurateClickHelper instanceof AccurateClickHelper &&
                    listener instanceof OnAccurateClickListener) {
                AccurateClickHelper ach = (AccurateClickHelper) accurateClickHelper;
                ((OnAccurateClickListener) listener).onAccurateClick(v, ach.x, ach.y);
            }
        }
    }

    private static class AccurateClickHelper implements Hotspotable {

        public float x;
        public float y;

        @Override
        public void setHotspot(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Interface definition for a callback to be invoked when a view is clicked.
     */
    public interface OnAccurateClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         * @param x The x of the click position
         * @param y The y of the click position
         */
        void onAccurateClick(View v, float x, float y);
    }
}
