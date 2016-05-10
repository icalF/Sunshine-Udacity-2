package com.example.android.sunshine.app;

/**
 * Created by user on 4/23/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {
  private static final int VIEW_TYPE_TODAY = 0;
  private static final int VIEW_TYPE_FUTURE_DAY = 1;
  private static final int VIEW_TYPE_COUNT = 2;
  private boolean mUseTodayLayout = true;

  public void setUseTodayLayout(boolean mUseTodayLayout) {
    this.mUseTodayLayout = mUseTodayLayout;
  }

  public ForecastAdapter(Context context, Cursor c, int flags) {
    super(context, c, flags);
  }

  /**
   * Cache of the children views for a forecast list item.
   */
  public static class ViewHolder {
    public final ImageView iconView;
    public final TextView dateView;
    public final TextView descriptionView;
    public final TextView highTempView;
    public final TextView lowTempView;

    public ViewHolder(View view) {
      iconView = (ImageView) view.findViewById(R.id.list_item_icon);
      dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
      descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
      highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
      lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
    }
  }

  @Override
  public int getItemViewType(int position) {
    return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
  }

  @Override
  public int getViewTypeCount() {
    return VIEW_TYPE_COUNT;
  }

  /**
   * Copy/paste note: Replace existing newView() method in ForecastAdapter with this one.
   */
  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    // Choose the layout type
    int viewType = getItemViewType(cursor.getPosition());
    int layoutId = -1;

    switch (viewType) {
      case VIEW_TYPE_TODAY :
        layoutId = R.layout.list_item_forecast_today;
        break;
      case VIEW_TYPE_FUTURE_DAY :
        layoutId = R.layout.list_item_forecast;
        break;
      default :
        throw new RuntimeException("No match item identified");
    }

    View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

    ViewHolder viewHolder = new ViewHolder(view);
    view.setTag(viewHolder);
    return view;
  }

  /*
        This is where we fill-in the views with the contents of the cursor.
     */
  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    // our view is pretty simple here --- just a text view
    // we'll keep the UI functional with a simple (and slow!) binding.

    ViewHolder viewHolder = (ViewHolder) view.getTag();

    // Use placeholder image for now
    int viewType = getItemViewType(cursor.getPosition());
    switch (viewType) {
      case VIEW_TYPE_TODAY :
        viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
        break;
      case VIEW_TYPE_FUTURE_DAY :
        viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
        break;
      default :
        throw new RuntimeException("No match item identified");
    }

    // Read date from cursor
    long date = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
    viewHolder.dateView.setText(Utility.getFriendlyDayString(context, date));

    // Read weather forecast from cursor
    String forecast = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
    viewHolder.descriptionView.setText(forecast);

    // For accessibility, add a content description to the icon field
    viewHolder.iconView.setContentDescription(forecast);

    // Read user preference for metric or imperial temperature units
    boolean isMetric = Utility.isMetric(context);

    // Read high temperature from cursor
    double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
    viewHolder.highTempView.setText(Utility.formatTemperature(context, high));

    // Read low temperature from cursor
    double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
    viewHolder.lowTempView.setText(Utility.formatTemperature(context, low));
  }
}
