package com.buaa.sensory.wsn_dr.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.util.Log;

public class GetDataClass {
	private static String ip = "115.28.143.212";
	private static int port = 8080;

	/*
	 * 功能编号1 查询指定节点的最新信息 requestAllNewData()
	 * sendRequdesAllNewMassage(DataOutputStream dout,byte count,String stime)
	 * receiveRespondAllNewMassage(DataInputStream din,NodeClass[] nodes)
	 */
	public static NodeClass[] requestAllNewData(String st, int[] nodeId)
			throws Exception { // 方法：连接客户端

		NodeClass[] nodes = null;

		Socket sc = null;
		DataInputStream din = null;
		DataOutputStream dout = null;
		try {
			sc = new Socket();// 创建Socket连接
			Log.v("Tag", "创建Socket连接");
			SocketAddress address = new InetSocketAddress(ip, port);
			sc.connect(address, 5000);// 设置连接超时1s
			sc.setSoTimeout(5000);// 设置读取超时5s

			din = new DataInputStream(sc.getInputStream());// 创建输入输出流对象
			dout = new DataOutputStream(sc.getOutputStream());

			// 发送数据请求，包含数据个数
			Log.v("Tag", "发送数据");
			dout.writeByte(0xEE);// head
			dout.writeByte((byte) (0x01)); // type
			dout.writeUTF(st);
			dout.writeByte(nodeId.length);
			for (int i = 0; i < nodeId.length; i++) {
				dout.writeInt(nodeId[i]);
			}

			// 接收数据，数据存于对象nodes中
			Log.v("Tag", "接收数据");
			byte head = din.readByte();
			byte style = din.readByte();// 操作类型
			int count = din.readByte();// 数据数
			nodes = new NodeClass[count];
			for (int i = 0; i < count; i++) {
				nodes[i] = new NodeClass();
				nodes[i].setId(din.readInt());
				nodes[i].setTime(din.readUTF());
				nodes[i].setGPSLong(din.readDouble());
				nodes[i].setGPSeast(din.readBoolean());
				nodes[i].setGPSLati(din.readDouble());
				nodes[i].setGPSnorth(din.readBoolean());
				nodes[i].setHigh(din.readDouble());
				nodes[i].setSpeed(din.readDouble());
				nodes[i].setLongi(din.readDouble());
				nodes[i].setEast(din.readBoolean());
				nodes[i].setLati(din.readDouble());
				nodes[i].setNorth(din.readBoolean());

			}
		} catch (Exception e) {
			e.printStackTrace();
			// Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
			Log.v("Tag", "socket异常");
		} finally {
			try {
				if (din != null) {
					din.close();
					din = null;
				}
				if (dout != null) {
					dout.close();
					dout = null;
				}
				if (sc != null) {
					sc.close();
					sc = null;
				}
				Log.v("Tag", "关闭Socket连接");
			} catch (Exception e2) {
				Log.e("Tag", "关闭Socket连接");
				e2.printStackTrace();
			}
		}
		return nodes;
	}

	/*
	 * 功能编号2 查询指定节点的历史信息 requestHistoryData()
	 * sendRequdesAllNewMassage(DataOutputStream dout,byte count,String stime)
	 * receiveRespondAllNewMassage(DataInputStream din,NodeClass[] nodes)
	 */
	public static NodeClass[] requestHistoryData(String st, String et,
			int[] nodeId) { // 方法：连接客户端
		// Log.v("Tag", "" + nodeId[0] + nodeId[1]);
		NodeClass[][] nodes_his = null;
		NodeClass[] nodes_his_sum = null;
		Socket sc = null;
		DataInputStream din = null;
		DataOutputStream dout = null;
		int num = 0;
		nodes_his = new NodeClass[nodeId.length][];
		for (int i = 0; i < nodeId.length; i++) {

			try {
				sc = new Socket();// 创建Socket连接
				Log.v("Tag", "创建Socket连接");
				SocketAddress address = new InetSocketAddress(ip, port);
				sc.connect(address, 5000);// 设置连接超时1s
				sc.setSoTimeout(5000);// 设置读取超时5s

				din = new DataInputStream(sc.getInputStream());// 创建输入输出流对象
				dout = new DataOutputStream(sc.getOutputStream());

				// 发送数据请求，包含数据个数
				Log.v("Tag", "发送数据");
				dout.writeByte(0xEE);// head
				dout.writeByte((byte) (0x02)); // type
				dout.writeInt(nodeId[i]);
				dout.writeUTF(st);
				dout.writeUTF(et);

				// 接收数据，数据存于对象nodes中
				Log.v("Tag", "接收数据");
				byte head = din.readByte();
				byte style = din.readByte();// 操作类型
				int id = din.readInt();
				int count = din.readByte();// 数据数
				nodes_his[i] = new NodeClass[count];
				Log.v("Tag", i + " " + count);
				for (int j = 0; j < count; j++) {
					nodes_his[i][j] = new NodeClass();
					nodes_his[i][j].setId(id);
					nodes_his[i][j].setTime(din.readUTF());
					nodes_his[i][j].setGPSLong(din.readDouble());
					nodes_his[i][j].setGPSeast(din.readBoolean());
					nodes_his[i][j].setGPSLati(din.readDouble());
					nodes_his[i][j].setGPSnorth(din.readBoolean());
					nodes_his[i][j].setHigh(din.readDouble());
					nodes_his[i][j].setSpeed(din.readDouble());
					nodes_his[i][j].setLongi(din.readDouble());
					nodes_his[i][j].setEast(din.readBoolean());
					nodes_his[i][j].setLati(din.readDouble());
					nodes_his[i][j].setNorth(din.readBoolean());

				}
				num = nodes_his[i].length + num;
				Log.v("Tag", "num=" + num);
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("Tag", "socket异常");
			} finally {
				try {
					if (din != null) {
						din.close();
						din = null;
					}
					if (dout != null) {
						dout.close();
						dout = null;
					}
					if (sc != null) {
						sc.close();
						sc = null;
					}
					Log.v("Tag", "关闭Socket连接");
				} catch (IOException e2) {
					Log.e("Tag", "关闭Socket连接");
					e2.printStackTrace();
				}
			}

		}

		// 将二维数组整合到一维数组里
		nodes_his_sum = new NodeClass[num];
		int num2 = 0;
		for (int i = 0; i < nodes_his.length; i++) {
			for (int j = 0; j < nodes_his[i].length; j++) {
				nodes_his_sum[num2] = nodes_his[i][j];
				num2++;
			}
		}

		Log.v("Tag", "num2=" + num2);
		return nodes_his_sum;
	}

	/*
	 * 功能编号4 查询从指定时间开始的所有活动节点 requestTopoData()
	 */
	public static NodeClass[] requestTopoData(String st) {
		NodeClass[] nodes = null;

		Socket sc = null;
		DataInputStream din = null;
		DataOutputStream dout = null;
		try {
			sc = new Socket();// 创建Socket连接
			Log.v("Tag", "创建Socket连接");
			SocketAddress address = new InetSocketAddress(ip, port);
			sc.connect(address, 5000);// 设置连接超时1s
			sc.setSoTimeout(5000);// 设置读取超时5s

			din = new DataInputStream(sc.getInputStream());// 创建输入输出流对象
			dout = new DataOutputStream(sc.getOutputStream());

			// 发送数据请求，包含数据个数
			Log.v("Tag", "发送数据");
			dout.writeByte(0xEE);// head
			dout.writeByte((byte) (0x04)); // type
			dout.writeUTF(st);

			// 接收数据，数据存于对象nodes中
			Log.v("Tag", "接收数据");
			byte head = din.readByte();
			byte style = din.readByte();// 操作类型
			int count = din.readByte();// 数据数
			nodes = new NodeClass[count];
			for (int i = 0; i < count; i++) {
				nodes[i] = new NodeClass();
				nodes[i].setId(din.readInt());
				nodes[i].setTime(din.readUTF());
				nodes[i].setTurnnode(din.readInt());
			}
		} catch (IOException e) {
			e.printStackTrace();
			// Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
			Log.e("Tag", "socket异常");
		} finally {
			try {
				if (din != null) {
					din.close();
					din = null;
				}
				if (dout != null) {
					dout.close();
					dout = null;
				}
				if (sc != null) {
					sc.close();
					sc = null;
				}
				Log.v("Tag", "关闭Socket连接");
			} catch (IOException e2) {
				Log.e("Tag", "关闭Socket连接");
				e2.printStackTrace();
			}
		}
		return nodes;
	}

	/*
	 * 功能编号2 查询指定节点的历史信息 requestHistoryData()
	 * sendRequdesAllNewMassage(DataOutputStream dout,byte count,String stime)
	 * receiveRespondAllNewMassage(DataInputStream din,NodeClass[] nodes)
	 */
	public static byte requestLogin(Byte LoginOrRegist, String name, String key) {
		Socket sc = null;
		DataInputStream din = null;
		DataOutputStream dout = null;
		byte permission = 0;
		try {
			sc = new Socket();// 创建Socket连接
			Log.v("Tag", "创建Socket连接");
			SocketAddress address = new InetSocketAddress(ip, port);
			sc.connect(address, 5000);// 设置连接超时1s
			sc.setSoTimeout(5000);// 设置读取超时5s

			din = new DataInputStream(sc.getInputStream());// 创建输入输出流对象
			dout = new DataOutputStream(sc.getOutputStream());

			// 发送数据请求，包含数据个数
			Log.v("Tag", "发送login数据");
			dout.writeByte(0xEE);// head
			dout.writeByte((byte) (0x07)); // type
			dout.writeByte((byte) (0x01)); // type
			dout.writeUTF("1");
			dout.writeUTF("2");

			// 接收数据，数据存于对象nodes中
			Log.v("Tag", "接收login数据");
			byte head = din.readByte();
			byte style = din.readByte();// 操作类型
			Log.v("Tag", "style" + style);
			permission = din.readByte();// 数据数
			Log.v("Tag", "permission" + permission);
			// String info = din.readUTF();
			// Log.v("Tag", "info" + info);

		} catch (IOException e) {
			e.printStackTrace();
			// Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
			Log.e("Tag", "socket异常");
		} finally {
			try {
				if (din != null) {
					din.close();
					din = null;
				}
				if (dout != null) {
					dout.close();
					dout = null;
				}
				if (sc != null) {
					sc.close();
					sc = null;
				}
				Log.v("Tag", "关闭Socket连接");
			} catch (IOException e2) {
				Log.e("Tag", "关闭Socket连接");
				e2.printStackTrace();
			}
		}
		return permission;
	}

}