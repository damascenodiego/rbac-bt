package com.usp.icmc.labes.utils;

import java.util.concurrent.TimeUnit;


// source http://silveiraneto.net/2008/03/15/simple-java-chronometer/

public final class Chronometer{
    private long begin, end;
 
    public void start(){
        begin = System.currentTimeMillis();
    }
 
    public void stop(){
        end = System.currentTimeMillis();
    }
 
    public long getTime() {
        return end-begin;
    }
 
    public long getMilliseconds() {
        return end-begin;
    }
 
    public double getInSeconds() {
        return TimeUnit.SECONDS.convert(end - begin, TimeUnit.MILLISECONDS);
    }
 
    public double getInMinutes() {
    	return TimeUnit.MINUTES.convert(end - begin, TimeUnit.MILLISECONDS);
    }
 
    public double getInHours() {
    	return TimeUnit.HOURS.convert(end - begin, TimeUnit.MILLISECONDS);
    }
 
    public static void main(String[] arg) {
        Chronometer ch = new Chronometer();
 
        ch.start();
        for (int i = 1;i<10000000;i++) {}
        ch.stop();
        System.out.println(ch.getTime());
 
        ch.start();
        for (int i = 10000000;i>0;i--) {}
        ch.stop();
        System.out.println(ch.getTime());
    }
}