package ctd.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ctd.controller.exception.ControllerException;

public class RootNode extends ResourceNode {
	private static final long serialVersionUID = 3396589217299305963L;
	private static final Logger logger = LoggerFactory.getLogger(RootNode.class);
	
	public RootNode(String id) {
		super(id);
	}
	
	@Override
	public boolean hasChild(String id){
		try {
			return CategoryNodeController.instance().get(id) != null;
		} 
		catch (ControllerException e) {
			if(e.isInstanceNotFound()){
				CategoryNodeController.instance().add(new CategoryNode(id));
				logger.warn("CatagoryNode[{}] not defined,init with empty node",id);
				return true;
			}
			else{
				logger.error(e.getMessage());
				return false;
			}
		}
	}
	
	@Override
	public ResourceNode getChild(String id){
		ResourceNode node;
		try {
			node = CategoryNodeController.instance().get(id);
		} 
		catch (ControllerException e) {
			if(e.isInstanceNotFound()){
				CategoryNode emptyNode = new CategoryNode(id);
				CategoryNodeController.instance().add(emptyNode);
				logger.info("CatagoryNode[{}] not defined,init with empty node",id);
				return emptyNode;
			}
			else{
				logger.error(e.getMessage());
			}
			return this;
		}
		if(node != null){
			return node;
		}
		else{
			return this;
		}
	}

}
