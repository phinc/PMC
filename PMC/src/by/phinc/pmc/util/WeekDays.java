/*
 * WeekDays
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 03.09.2012
 * 
 * The class provides functionality for work with days of week.
 * Class is not thread safe.
 */
package by.phinc.pmc.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;

public class WeekDays implements Iterable<WeekDays> {
	
	public static final int NUMBER_DAYS_IN_WEEK = 7;
	
	public static final int INIT_VALUE = -1;
	
	private Locale locale;
	
	private Date date;
	
	private Calendar calendar;
	
	private int step = 1;
	
	//time period for iterating over
	private int period = NUMBER_DAYS_IN_WEEK;
	
	
	public WeekDays(Date curDate) {
		date = curDate;
		locale = Locale.getDefault();
		calendar = new GregorianCalendar(locale);
		calendar.setTime(curDate);
	}
	
	public WeekDays(Date curDate, Locale locale) {
		date = curDate;
		this.locale = locale;
		calendar = new GregorianCalendar(locale);
		calendar.setTime(curDate);
	}
	
	
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}
	
	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	/*
	 * This field takes int values SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, 
	 * FRIDAY, and SATURDAY defined in java.util.Calendar
	 */
	public void setFirstDayOfWeek(int weekDay) {
		calendar.setFirstDayOfWeek(weekDay);
	}
	
	
	public String getShortDayName() {
		return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale);
	}
	
	
	public Date getCurDate() {
		return calendar.getTime();
	}


	@Override
	public Iterator<WeekDays> iterator() {
		return new Iterator<WeekDays>(){
			
			private int idx = INIT_VALUE;
			
			
			@Override
			public boolean hasNext() {
				return idx < period;
			}

			@Override
			public WeekDays next() {
				if (idx == INIT_VALUE) {
					idx = 1;
					calendar.setTime(date);
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
				} else {
					calendar.add(Calendar.DATE, step);
					idx++;
				}
				return WeekDays.this;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException(
						"You can't delete a day from a week");
			}
			
		};
	}
	
}
