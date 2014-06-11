package ctd.dao.support.dictionary;

import java.util.HashMap;
import java.util.Map;

import ctd.controller.Controllers;
import ctd.controller.exception.ControllerException;
import ctd.controller.notifier.ConfigurableNotifier;
import ctd.controller.notifier.NotifierCommands;
import ctd.controller.notifier.NotifierMessage;
import ctd.dao.exception.DAOException;
import ctd.dao.support.hibernate.HibernateSupportDAO;
import ctd.dao.support.hibernate.HibernateSupportReadDAO;
import ctd.dao.support.hibernate.HibernateSupportWriteDAO;
import ctd.dictionary.DictionaryController;
import ctd.dictionary.DictionaryItem;
import ctd.dictionary.support.DatabaseDictionary;

public class DictionarySupportDAO extends HibernateSupportDAO<Map<String,Object>> {
	private String dictionaryId;
	
	public DictionarySupportDAO(){
		setEntityClass(HashMap.class);
		setReadDAO(new HibernateSupportReadDAO<Map<String,Object>>());
		setWriteDAO(new HibernateSupportWriteDAO<Map<String,Object>>(){
			@Override
			public void afterSave(Map<String,Object> o) throws DAOException{
				this.afterUpdate(o);
			}
			
			@Override
			public void afterUpdate(Map<String, Object> o)  throws DAOException{
				ConfigurableNotifier notifier = DictionaryController.instance().getNotifier();
				NotifierMessage message = new NotifierMessage(NotifierCommands.ITEM_UPDATE,dictionaryId);
		
				DictionaryItem di = createDictionaryItem(o);
				long lastModify = System.currentTimeMillis();
				
				message.addUpdatedItems(di);
				message.setLastModify(lastModify);
				
				notifier.notifyMessage(message);
			}
			
			protected void afterRemove(Object id)  throws DAOException{
				ConfigurableNotifier notifier = DictionaryController.instance().getNotifier();
				long lastModify = System.currentTimeMillis();
				
				NotifierMessage message = new NotifierMessage(NotifierCommands.ITEM_REMOVE,dictionaryId);
				message.addUpdatedItems(id);
				message.setLastModify(lastModify);
				notifier.notifyMessage(message);
			
			}
			
			public DictionaryItem createDictionaryItem(Map<String,Object> map)  throws DAOException{
				try {
					DatabaseDictionary dic = (DatabaseDictionary) Controllers.getDictionary(dictionaryId);
					return dic.getLoader().createDictionaryItem(map);
				} 
				catch (ControllerException e) {
					throw new DAOException(e);
				}
			}
		});
	}

	public String getDictionaryId() {
		return dictionaryId;
	}

	public void setDictionaryId(String dictionaryId) {
		this.dictionaryId = dictionaryId;
	}

}
