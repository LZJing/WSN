package com.buaa.sensory.wsn_dr.model;

public class MoveType {
	int  nodeId;
	int type;
	public int getNodeId() {
		return nodeId;
	}
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "MoveType [nodeId=" + nodeId + ", type=" + type + "]";
	}
	
	
}
