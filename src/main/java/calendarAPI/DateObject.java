package calendarAPI;

public class DateObject {

	private String start;
	private String end;

	public DateObject(String start, String end) {
		this.start = start;
		this.end = end;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "DateObject [start=" + start + ", end=" + end + "]";
	}

}
