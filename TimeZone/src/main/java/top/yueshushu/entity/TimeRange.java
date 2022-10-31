package top.yueshushu.entity;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * @author wanyanhw
 * @since 2021/9/22 17:54
 */
@Data
public class TimeRange implements Serializable {
	public TimeRange() {
	}

	public TimeRange(String start, String end) {
		this.start = start;
		this.end = end;
	}

	private String start;

	private String end;

	@Override
	public String toString() {
		return start + "," + end;
	}

	public boolean anyEmpty() {
		return StringUtils.isEmpty(start) || StringUtils.isEmpty(end);
	}

	public LocalTime[] getLocalTimes(String separator) throws Exception {
		LocalTime[] localTimes = new LocalTime[2];
		String[] startTime = start.split(separator);
		String[] endTime = end.split(separator);
		localTimes[0] = LocalTime.of(Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]), 0);
		localTimes[1] = LocalTime.of(Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]), 59);
		return localTimes;
	}
}
