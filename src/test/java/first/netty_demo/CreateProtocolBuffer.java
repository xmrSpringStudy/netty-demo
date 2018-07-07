package first.netty_demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class CreateProtocolBuffer {

    public static void main(String[] args) throws Exception {
    	HeaderProtocol.Header.MsgType.Builder msgTypeBuilder = HeaderProtocol.Header.MsgType.newBuilder();
    	msgTypeBuilder.setType(HeaderProtocol.Header.MsgType.MessageType.REQUEST);
    	HeaderProtocol.Header.Builder headerBuilder = HeaderProtocol.Header.newBuilder();
    	headerBuilder.setMsgType(msgTypeBuilder);
    	headerBuilder.setCseq(1);
    	headerBuilder.setVersion(1);
    	EquipmentProtocol.EquipmentConfig.Builder equiomentConfigBuilder = EquipmentProtocol.EquipmentConfig.newBuilder();
    	equiomentConfigBuilder.setHeader(headerBuilder);
    	equiomentConfigBuilder.setEquipmentID("136_789_54234");
    	equiomentConfigBuilder.setConfigInformation("{test:\"this\",name:\"is a test\",value:37}");
    	EquipmentProtocol.EquipmentConfig equipmentConfig = equiomentConfigBuilder.build();
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
    	equipmentConfig.writeTo(output);
		
		// -------------- 分割线：上面是发送方，将数据序列化后发送 ---------------
		
		byte[] byteArray = output.toByteArray();
		
		File file = new File("protocol_test.bin");
		 if (file.exists()) {
			          file.delete();
		 }
		
		file.createNewFile();
		
		OutputStream outputStream = new FileOutputStream(file);
		
		outputStream.write(byteArray);
		outputStream.close();

    }

}
