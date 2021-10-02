package org.example.design;


public class TokenBucket {
	private final long maxBucketSize;
	private final long refillRate;
	private double currentBucketSize;
	private long lastRefillTimestamp;
	
	
	public TokenBucket(long maxBucketSize, long refillRate) {
		this.maxBucketSize = maxBucketSize;
		this.refillRate = refillRate;
		this.currentBucketSize = maxBucketSize;
		this.lastRefillTimestamp = System.nanoTime();
	}
	
	public synchronized boolean allowRequest(int tokens) {	// tokens = the cost of the operation
		refill(); 											// initially refill the bucket with tokens accumulated since the last call

		if (currentBucketSize > tokens) {					// if bucket has enough tokens, the call is allowed
			currentBucketSize -= tokens;
			return true;
		}
		else {
			return false; // request is throttled as the bucket doesn't have enough tokens
		}
	}
	
	
	private void refill() {
		long now=System.nanoTime();double tokensToAdd=(now-lastRefillTimestamp)*refillRate/1_000_000_000;
		currentBucketSize=Math.min(currentBucketSize+tokensToAdd,maxBucketSize);lastRefillTimestamp=now;
	}
	
}
