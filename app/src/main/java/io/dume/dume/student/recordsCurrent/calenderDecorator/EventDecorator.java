package io.dume.dume.student.recordsCurrent.calenderDecorator;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

  private int color;
  private HashSet<CalendarDay> dates;
  private Context context;


  public EventDecorator(int color, Collection<CalendarDay> dates, Context context) {
    this.color = color;
    this.dates = new HashSet<>(dates);
    this.context = context;
  }

  @Override
  public boolean shouldDecorate(CalendarDay day) {
    return dates.contains(day);
  }

  @Override
  public void decorate(DayViewFacade view) {
    view.addSpan(new DotSpan((int) (3.5 * (context.getResources().getDisplayMetrics().density)), color));
  }
}
