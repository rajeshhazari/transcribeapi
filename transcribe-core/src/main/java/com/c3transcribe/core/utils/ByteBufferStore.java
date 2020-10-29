package com.c3transcribe.core.utils;

public interface ByteBufferStore {
        byte[] takeBuffer(int bufferSize);
        void putBuffer(byte[] buffer);
}
