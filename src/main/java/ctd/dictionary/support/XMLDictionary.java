package ctd.dictionary.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import ctd.dictionary.DictionaryItem;
import ctd.dictionary.SliceTypes;

public class XMLDictionary extends AbstractDictionary {
	
	private static final long serialVersionUID = -596194603210170948L;
	private static final String LEAF_CNDS = "count(./*) = 0";
	private static final String FOLDER_CNDS = "count(./*) > 0";
	protected Document defineDoc;
	
	public XMLDictionary(){
	}
	
	public XMLDictionary(String id){
		setId(id);
	}

	public void setDefineDoc(Document doc){
		this.defineDoc = doc;
	}
	
	public Document getDefineDoc(){
		return defineDoc;
	}
	
	@Override
	protected  void onItemUpdate(DictionaryItem item){
		Element root = defineDoc.getRootElement();
		Element el = (Element) root.selectSingleNode("//item[@key='" + item.getKey() + "']");
		if(el != null){
			el.getParent().remove(el);
		}
		else{
			el = DocumentHelper.createElement("item");
			el.addAttribute("key", item.getKey());
		}
		el.addAttribute("text",item.getText());
		el.addAttribute("mCode", item.getMCode());
		Map<String,Object> properties = item.getProperties();
		if(properties != null){
			Set<String> keys = properties.keySet();
			for(String nm : keys){
				Element p = (Element) el.selectSingleNode("p[@name='" + nm  +"']");
				if(p == null){
					p = DocumentHelper.createElement("p");
					p.addAttribute("name", nm);
				}
				p.setText(item.getProperty(nm, String.class));
			}
		}
		
		String parentKey = item.getParent();
		Element parentEl = null;
		if(StringUtils.isEmpty(parentKey)){
			parentEl = root;
		}
		else{
			parentEl = (Element) root.selectSingleNode("//item[@key='" + parentKey + "']");
		}
		if(parentEl != null){
			parentEl.add(el);
		}
	}
	
	@Override
	protected  void onItemRemoved(String key){
		Element root = defineDoc.getRootElement();
		Element el = (Element) root.selectSingleNode("//item[@key='" + key + "']");
		if(el != null){
			el.getParent().remove(el);
		}
	}
	
	@Override
	public List<DictionaryItem> getSlice(String parentKey, int sliceType,String query) {
		List<DictionaryItem> result = null;
		switch(sliceType){
			case SliceTypes.ALL_FOLDER:
				result = getAllFolder(parentKey,query);
				break;
			
			case SliceTypes.ALL_LEAF:
				result = getAllLeaf(parentKey,query);
				break;
				
			case SliceTypes.CHILD_ALL:
				result = getAllChild(parentKey,query);
				break;
			
			case SliceTypes.CHILD_FOLDER:
				result = getChildFolder(parentKey,query);
				break;
			
			case SliceTypes.CHILD_LEAF:
				result = getChildLeaf(parentKey,query);
				break;
			
			default:
				result = getAllItems(parentKey,query);
		}
		
		return result;
	}
	
	private void linkQueryXPath(StringBuffer xpath,String query,String exCnd){
		if(!StringUtils.isEmpty(query)){
			xpath.append("contains(@");
			char first = query.charAt(0);
			if(first == searchKeySymbol){
				xpath.append("key,").append(query.substring(1));
			}
			else if(first == searchExSymbol){
				xpath.append(searchFieldEx).append(",'").append(query.substring(1));
			}
			else{
				xpath.append(searchField).append(",'").append(query);
			}
			xpath.append("')");
			if(!StringUtils.isEmpty(exCnd)){
				xpath.append(" and ").append(exCnd);
			}
		}
		else{
			if(!StringUtils.isEmpty(exCnd)){
				xpath.append(exCnd);
			}
		}
	}
	
	private List<DictionaryItem> toDictionaryItemList(List<Element> ls){
		List<DictionaryItem> result = new ArrayList<DictionaryItem>();
		
		for(Element el : ls){
			String key = el.attributeValue("key");
			result.add(items.get(key));
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private List<DictionaryItem> getAllItems(String parentKey,String query){
		if(defineDoc  == null){
			return null;
		}
		Element define = defineDoc.getRootElement();
	
		StringBuffer xpath = new StringBuffer();
		if(!StringUtils.isEmpty(parentKey)){
			xpath.append("//item[@key='").append(parentKey).append("']");
		}
		
		if(!StringUtils.isEmpty(query)){
			xpath.append("//item[");
			linkQueryXPath(xpath,query,null);
			xpath.append("]");
		}
		else{
			xpath.append("//item");
		}
		List<Element> ls = define.selectNodes(xpath.toString());
		return toDictionaryItemList(ls);
	}
	
	@SuppressWarnings("unchecked")
	private List<DictionaryItem> getAllChild(String parentKey,String query){
		if(defineDoc  == null){
			return null;
		}
		Element define = defineDoc.getRootElement();

		StringBuffer xpath = new StringBuffer();
		if(!StringUtils.isEmpty(parentKey)){
			xpath.append("//item[@key='").append(parentKey).append("']");
		}
		else{
			xpath.append(".");
		}
		
		if(!StringUtils.isEmpty(query)){
			xpath.append("/item[");
			linkQueryXPath(xpath,query,null);
			xpath.append("]");
		}
		else{
			xpath.append("/item");
		}
		
		List<Element> ls = define.selectNodes(xpath.toString());
		return toDictionaryItemList(ls);
	}
	
	@SuppressWarnings("unchecked")
	private List<DictionaryItem> getAllLeaf(String parentKey,String query){
		if(defineDoc  == null){
			return null;
		}
		Element define = defineDoc.getRootElement();
		StringBuffer xpath = new StringBuffer();
		if(!StringUtils.isEmpty(parentKey)){
			xpath.append("//item[@key='").append(parentKey).append("']/item[");
		}
		else{
			xpath.append("//item[");
		}
		linkQueryXPath(xpath,query,LEAF_CNDS);
		xpath.append("]");
	
		List<Element> ls = define.selectNodes(xpath.toString());
		return toDictionaryItemList(ls);
	}
	
	@SuppressWarnings("unchecked")
	private List<DictionaryItem> getAllFolder(String parentKey,String query){
		if(defineDoc  == null){
			return null;
		}
		Element define = defineDoc.getRootElement();

		StringBuffer xpath = new StringBuffer();
		if(!StringUtils.isEmpty(parentKey)){
			xpath.append("//item[@key='").append(parentKey).append("']//item[");
		}
		else{
			xpath.append("//item[");
		}
		linkQueryXPath(xpath,query,FOLDER_CNDS);
		xpath.append("]");
		List<Element> ls = define.selectNodes(xpath.toString());	
		return toDictionaryItemList(ls);
	}
	
	@SuppressWarnings("unchecked")
	private List<DictionaryItem> getChildFolder(String parentKey,String query){
		if(defineDoc == null){
			return null;
		}
		Element define = defineDoc.getRootElement();
		StringBuffer xpath = new StringBuffer();
		if(!StringUtils.isEmpty(parentKey)){
			xpath.append("//item[@key='").append(parentKey).append("']/item[");
		}
		else{
			xpath.append("item[");
		}
		linkQueryXPath(xpath,query,FOLDER_CNDS);
		xpath.append("]");
		List<Element> ls = define.selectNodes(xpath.toString());	
		return toDictionaryItemList(ls);
	}
	
	@SuppressWarnings("unchecked")
	private List<DictionaryItem> getChildLeaf(String parentKey,String query){
		if(defineDoc  == null){
			return null;
		}
		Element define = defineDoc.getRootElement();

		StringBuffer xpath = new StringBuffer();
		if(!StringUtils.isEmpty(parentKey)){
			xpath.append("//item[@key='").append(parentKey).append("']/item[");
		}
		else{
			xpath.append("item[");
		}
		linkQueryXPath(xpath,query,LEAF_CNDS);
		xpath.append("]");
		List<Element> ls = define.selectNodes(xpath.toString());
		return toDictionaryItemList(ls);
	}

}
