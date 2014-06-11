package ctd.dictionary;

import ctd.controller.exception.ControllerException;
import ctd.dictionary.service.DictionarySliceRecordSet;
import ctd.util.annotation.RpcService;

public interface DictionaryService {
	
	@RpcService
	public DictionarySliceRecordSet getSlice(String dicId,String parentKey,int sliceType,String query,int start,int limit) throws ControllerException;
	
	@RpcService
	public long getLastModify(String dicId) throws ControllerException;
}
