package com.qi4l.JYso.web;

import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RequestLogCollector {

    private static final int MAX_LINES = 200;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final List<String> lines = new ArrayList<>();
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static boolean installed = false;

    public static synchronized void install() {
        if (installed) return;
        installed = true;
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(new TeeOutputStream(originalOut), true));
    }

    private static void addLine(String rawLine) {
        String clean = rawLine
                .replaceAll("\u001B\\[[;\\d]*m", "")
                .replaceAll("\u001B\\[[;\\d]*[@-~]", "")
                .trim();
        if (clean.isEmpty()) return;

        String timestamp = LocalTime.now().format(TIME_FMT);
        String entry = "[" + timestamp + "] " + clean;

        lock.writeLock().lock();
        try {
            lines.add(entry);
            while (lines.size() > MAX_LINES) {
                lines.remove(0);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static List<String> getLines(int count) {
        lock.readLock().lock();
        try {
            int size = lines.size();
            if (size <= count || count <= 0) {
                return new ArrayList<>(lines);
            }
            return new ArrayList<>(lines.subList(size - count, size));
        } finally {
            lock.readLock().unlock();
        }
    }

    private static class TeeOutputStream extends OutputStream {
        private final OutputStream target;

        TeeOutputStream(OutputStream target) {
            this.target = target;
        }

        private final StringBuilder lineBuffer = new StringBuilder();

        @Override
        public void write(int b) {
            try {
                target.write(b);
            } catch (Exception ignored) {
            }
            if (b == '\n' || b == '\r') {
                if (lineBuffer.length() > 0) {
                    String line = lineBuffer.toString();
                    lineBuffer.setLength(0);
                    if (!line.isEmpty()) {
                        addLine(line);
                    }
                }
            } else {
                lineBuffer.append((char) b);
            }
        }

        @Override
        public void write(byte[] buf, int off, int len) {
            try {
                target.write(buf, off, len);
            } catch (Exception ignored) {
            }
            for (int i = off; i < off + len; i++) {
                char c = (char) buf[i];
                if (c == '\n' || c == '\r') {
                    if (lineBuffer.length() > 0) {
                        addLine(lineBuffer.toString());
                        lineBuffer.setLength(0);
                    }
                } else {
                    lineBuffer.append(c);
                }
            }
        }

        @Override
        public void flush() {
            try {
                target.flush();
            } catch (Exception ignored) {
            }
        }
    }
}
