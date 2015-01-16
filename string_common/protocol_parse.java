

通信协议

--------------------------------------------------------------------------
帧头    源地址   目的地址   帧类型   内容长度   正文内容    帧尾
--------------------------------------------------------------------------
1字节   1字节    1字节      1字节    2字节      不定长      1字节
0xaf                                                        0x5f
--------------------------------------------------------------------------



方法一：
在接收端先找到0xaf字节表示一帧开始，接着再找下一个0x5f字节，如果找到，就把该区间的内容作为一帧进行进一步解析（地址、帧类型、内容长度、内容）。
然后以0x5f为基准，接着找下一帧。

该种解析方法的优点是思路简单，缺点是如果内容包含字节0xaf或0x5f时，该方法不能准确地从粘包中提取出内容

for(i=0;i<len_buffer_socket_parse;i++) {
	if(buffer_socket_parse[i]==Packat_Header) {
		index_begin=i;
		index_temp=index_begin;
		for(;index_temp<len_buffer_socket_parse;index_temp++) {
			if(buffer_socket_parse[index_temp]==Packat_End) {
				index_end=index_temp;
				//发送到socket处理
				for(cursor=index_begin,j=0;cursor<=index_end;cursor++,j++) {
					socket_frame_content[j]=buffer_socket_parse[cursor];
					SLOGE("cursor=%d,socket_frame_content[%d]=%x",cursor,j,socket_frame_content[j]);
				}

				SLOGE("socket_frame_content[0]=%x,socket_frame_content[%d]=%d",socket_frame_content[0],j-1,socket_frame_content[j-1]);
				addr = socket_frame_content[1]>>2;
				contentlen = socket_frame_content[2]; //正文长度

				for(index=0;index<contentlen;index++) {
					content[index]=socket_frame_content[index+3];
					SLOGE("addr=%d,contentlen=%d,content[%d]=%x",addr,contentlen,index,content[index]);
				}

				switch(addr) {
					case FRONTPANEL_ADDR:
						SLOGE("COMMAND TO computer");
						break;
					default:
						SLOGE("ERROR:TWE_Socket_Loop switch");
						break;
				}

				i=index_end;
				break;
			}
		}
	}
}




方法二：
从接收缓冲区接收到数据，无论粘包或是不粘包，第一个字节必然是0xaf。基于这个前提，我们可以从0xaf开始，
使用状态机，依据协议一个字节一个字节的开始解析缓冲区的数据，依据内容长度找出内容。

该方法的缺点是在基于跨网络的socket通信中可能会出现问题，
优点是可以准确地识别出内容，即使内容中包含0xaf或0x5f。


if(len_buffer_socket_parse>0) {
	SLOGE("buffer_socket_parse[0]=%x,s_fdCommand=%d",buffer_socket_parse[0],s_fdCommand);
	for(i=0;i<len_buffer_socket_parse;i++) {
		dataIn = buffer_socket_parse[i];
		SLOGE("status_socket=%d,dataIn=%x",status_socket,dataIn);
		switch(status_socket) {
			case 0:
				if(dataIn==Packat_Header) {
					status_socket++;
					index=0; //要记得清零，我在这里犯过一次错误
				}else{
					i=len_buffer_socket_parse; //该数据报有问题、丢弃
					SLOGE("第一个字节有误");
				}
				break;
			case 1: //源地址
				if(dataIn==HIFIPANEL_ADDR||dataIn==FRONTPANEL_ADDR||dataIn==IR_ADDR||dataIn==ANDROIDPANEL_ADDRESS) {
					source_addr=dataIn;
					status_socket++;
				}else{
					status_socket=0;
					i=len_buffer_socket_parse;
					SLOGE("源地址有误");
				}
				break;
			case 2: //目的地址
				if(dataIn==HIFIPANEL_ADDR||dataIn==FRONTPANEL_ADDR||dataIn==IR_ADDR) {
					dest_addr=dataIn;
					status_socket++;
				}else{
					SLOGE("目的地址有误");
					status_socket=0;
					i=len_buffer_socket_parse;
				}
				break;
			case 3://帧类型
				if(dataIn==CommPackType_ACK||dataIn==CommPackType_NoAck||dataIn==CommPackType_EachFrameACK||dataIn==CommPackType_LastAck) {
					frame_type=dataIn;
					status_socket++;
				}else{
					SLOGE("帧类型有误");
					status_socket=0;
					i=len_buffer_socket_parse;
				}
				break;
			case 4: //内容长度
				contentlen=dataIn;
				contentlen_temp=contentlen;
				status_socket++;
				break;
			case 5: //内容获取
				content[index]=dataIn;
				index++;
				if(--contentlen_temp==0) {
					status_socket++;
					for(j=0;j<contentlen;j++) {
						SLOGE(" to send content[%d]=%x",j,content[j]);
					}
				}
				break;
			case 6://帧尾
				if(dataIn==Packet_End) {
					status_socket=0;
					SLOGE("dest_addr=%d",dest_addr);
							
					switch(dest_addr) {
						case COMPUTER_ADDR: 	//发送到电脑
							SLOGE("COMMAND TO computer");
							break;
						default:
							SLOGE("ERROR:TWE_Socket_Loop send error");
							break;
					}
				}else{
					status_socket=0; //丢弃
					i=len_buffer_socket_parse;
				}
				break;
			default:
				break;
		}
					
	}
}
len_buffer_socket_parse = 0;








