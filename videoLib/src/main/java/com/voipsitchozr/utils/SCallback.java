package com.voipsitchozr.utils;

import java.io.IOException;

public interface SCallback {

	void callbackPreviewFrames(byte[] data);
	void callbackSurfaceCreated() throws IOException;
}
