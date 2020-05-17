package com.jiangfucheng.im.common.util;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/16
 * Time: 17:42
 * 雪花算法
 * 从左到右:
 * 1. 1bit 不用
 * 2. 41bit 当前时间戳
 * 3. 10bit 工作机器id，由5bit workerId和5bit dataCenterId组成，要保证每个机器的id都是不相同的，可以有 2^10=1024个节点同时使用
 * 4. 12bit 同一时间内id的序列号
 *
 * @author jiangfucheng
 */
public class SnowFlakeIdGenerator {
	private final long INIT_TIMESTAMP = 1589623371906L;

	private long dataCenterId;
	private long workerId;
	private long sequence;

	public SnowFlakeIdGenerator(long dataCenterId, long workerId) {
		this.dataCenterId = dataCenterId;
		this.workerId = workerId;
		this.sequence = 0;
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",maxWorkerId));
		}
		if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
			throw new IllegalArgumentException(String.format("data center Id can't be greater than %d or less than 0",maxDataCenterId));
		}
	}

	private long workerIdBits = 5;
	private long dataCenterIdBits = 5;
	private long sequenceIdBits = 12;

	private long maxWorkerId = ~(-1L << workerIdBits);
	private long maxDataCenterId = ~(-1L << dataCenterIdBits);
	private long maxSequenceId = ~(-1L << sequenceIdBits);

	private long lastTimestamp = -1L;
	private long dataCenterIdShiftBits = sequenceIdBits;
	private long workerIdShiftBits = sequenceIdBits + dataCenterIdBits;
	private long timestampShiftBits = sequenceIdBits + workerIdShiftBits + dataCenterIdBits;

	public Long nextId() {
		long timestamp = timeGenerate();
		if (timestamp == lastTimestamp) {
			//防止溢出
			sequence = (sequence + 1) & maxSequenceId;
			//这种情况说明,已经用完了当前毫秒数内的4095个id，去获取下一毫秒的id
			if (sequence == 0) {
				timestamp = tilNextMillis();
			}
		} else {
			sequence = 0;
		}
		lastTimestamp = timestamp;
		return ((timestamp - INIT_TIMESTAMP) << timestampShiftBits)
				| (workerId << workerIdShiftBits)
				| (dataCenterId << dataCenterIdShiftBits)
				| sequence;
	}

	private long tilNextMillis() {
		long curTimestamp = timeGenerate();
		while (curTimestamp <= lastTimestamp) {
			curTimestamp = timeGenerate();
		}
		return curTimestamp;
	}

	private long timeGenerate() {
		return System.currentTimeMillis();
	}

}
