package ctd.dictionary.service;

import java.util.List;

import ctd.controller.exception.ControllerException;
import ctd.dictionary.Dictionary;
import ctd.dictionary.DictionaryController;
import ctd.dictionary.DictionaryItem;
import ctd.dictionary.DictionaryService;
import ctd.util.annotation.RpcService;


public class DictionaryLocalService implements DictionaryService{
	
	@RpcService
	public DictionarySliceRecordSet getSlice(String dicId,String parentKey,int sliceType,String query,int start,int limit) throws ControllerException{
		Dictionary dic = DictionaryController.instance().get(dicId);
		
		List<DictionaryItem> ls = dic.getSlice(parentKey, sliceType, query);

		int size = ls == null ? 0 : ls.size();

		if(limit > 0 && size > 0){	
			if(start < 0){
				start = 0;
			}
			int offset = start + size;
			if(start + limit > size){
				offset = size;
			}
			ls = ls.subList(start, offset);
		}
		
		DictionarySliceRecordSet result = new DictionarySliceRecordSet();
		result.setDicId(dicId);
		result.setItems(ls);
		result.setStart(start);
		result.setLimit(limit);
		result.setTotal(size);
		result.setLastModify(dic.getlastModify());
		
		return result;
	}
	
	@RpcService
	public long getLastModify(String dicId) throws ControllerException{
		Dictionary dic = DictionaryController.instance().get(dicId);
		return dic.getlastModify();
	}
}
