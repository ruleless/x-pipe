package com.ctrip.xpipe.api.payload;



import java.io.IOException;
import java.nio.channels.WritableByteChannel;

import io.netty.buffer.ByteBuf;

/**
 * @author wenchao.meng
 *
 * 2016年3月29日 下午4:30:45
 */
public interface InOutPayload {


	void startInput();
	
	int in(ByteBuf byteBuf) throws IOException;
	
	void endInput();
	

	void startOutput();

	long out(WritableByteChannel writableByteChannel) throws IOException;
	
	void endOutput();

}
