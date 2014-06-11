package ctd.security;


public class Repository {

	private static ResourceNode root = new RootNode("reponsitory");
	
	public static ResourceNode getNode(String ...paths){
		if(paths.length == 0){
			return root;
		}
		ResourceNode node = root;
		for(String path : paths){
			if(node.hasChild(path)){
				node = node.getChild(path);
			}
		}
		return node;
	}
}
