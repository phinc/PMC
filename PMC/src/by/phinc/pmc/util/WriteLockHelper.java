package by.phinc.pmc.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WriteLockHelper {
	
	private static WriteLockHelper instance;
	
	private final Map<String, ReadWriteLock> locks = new HashMap<String, ReadWriteLock>();
	
	private WriteLockHelper(){};
	
	public static WriteLockHelper getInstance() {
		if (instance == null) {
			instance = new WriteLockHelper();
		}
		return instance;
	}
	
	public ReadWriteLock getReadWriteLock(String filename) {
		ReadWriteLock lock = null;
		synchronized (locks) {
			lock = locks.get(filename);
			if (lock == null) {
				lock = new ReentrantReadWriteLock();
				locks.put(filename, lock);
			}
		}		
		return lock;
	}

}
