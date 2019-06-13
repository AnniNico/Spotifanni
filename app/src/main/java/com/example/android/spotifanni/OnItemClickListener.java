package com.example.android.spotifanni;

import android.view.View;

/**
 * Created by nicolo.mandrile on 09/06/2019.
 * Interface used to handle RecyclerView Items events.
 */

public interface OnItemClickListener {

    void onItemClick(VideoItem item);

    void onAddClick(final View v, final VideoItem item);

    void onHearthClick(View v, VideoItem item);

    boolean onLongClicked(View v, VideoItem item);

}