package ctd.dictionary.support.databse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.StatelessSession;

import ctd.controller.exception.ControllerException;
import ctd.dao.exception.DAOException;
import ctd.dao.support.hibernate.HqlUtils;
import ctd.dao.support.hibernate.template.HibernateSessionTemplate;
import ctd.dao.support.hibernate.template.HibernateStatelessAction;
import ctd.dictionary.DictionaryItem;
import ctd.schema.Schema;
import ctd.schema.SchemaController;
import ctd.schema.SchemaItem;
import ctd.schema.constants.DisplayModes;

public class HibernateSupportDBDictionaryItemLoader extends AbstractDBDictionaryItemLoader{
	
	public DictionaryItem createDictionaryItem(Map<String,Object> map){
		try {
			DictionaryItem di = new DictionaryItem();
			Schema sc = SchemaController.instance().get(dictionaryInfo.getSchemaId());
			List<SchemaItem> items = sc.getItems();
			for(SchemaItem it : items){
				if(it.getDisplayMode() == DisplayModes.NO_LIST_DATA){
					continue;
				}
				String nm = it.getId();
				Object v = map.get(nm);
				configDictionaryItem(di, nm, v);
			}
			return di;
		} 
		catch (ControllerException e) {
			return null;
		}
	}
	
	@Override
	public List<DictionaryItem> loadItems(final List<?> cnds) throws ControllerException {
		final List<DictionaryItem> rs = new ArrayList<DictionaryItem>();
		
		try {
			HibernateSessionTemplate.instance().execute(new HibernateStatelessAction() {
				@Override
				public void execute(StatelessSession ss) throws Exception {
					Schema sc = SchemaController.instance().get(dictionaryInfo.getSchemaId());
					List<String> fields = dictionaryInfo.getFields();
					String orderBy = dictionaryInfo.getOrderBy();
					
					Query q = HqlUtils.buildSelectQuery(ss, sc.getEntityName(), fields, null, cnds, orderBy);
					
					
					int fieldCount = fields.size();
					ScrollableResults cursor  = q.setReadOnly(true).setFetchSize(Integer.MIN_VALUE).setCacheable(false).scroll(ScrollMode.FORWARD_ONLY);
					while(cursor.next()){
						Object[] r = cursor.get();
						DictionaryItem di = new DictionaryItem();
						for(int i = 0; i < fieldCount; i ++){
							String nm = fields.get(i);
							Object v = r[i];
							configDictionaryItem(di,nm,v);
						}
						rs.add(di);
					}
					
				}
				
			});
		} 
		catch (DAOException e) {
			throw new ControllerException(e,"load datebase dictionaryItem from [" + dictionaryInfo.getSchemaId() + "] falied.");
		}
		return rs;
	}


}
