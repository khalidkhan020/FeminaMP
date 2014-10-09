package  com.linchpin.periodttracker.database;



import java.util.Arrays;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Binder;
import android.support.v4.database.DatabaseUtilsCompat;

public class DBProvider extends ContentProvider
{
	private PeriodTrackerDBHandler	mOpenHelper;
	private static final String		UNKNOWN_URI_LOG	= "Unknown URI ";
	private static final String		THIS_FILE		= "DBProvider";
	
	private static final int		PERIOD_TRACKER	= 1, PERIOD_TRACKER_ID = 2;
	private static final int		DAY_DETAILS		= 3, DAY_DETAILS_ID = 4;
	private static final int		MOOD			= 5, MOOD_ID = 6;
	private static final int		MOOD_SELECTED	= 7, MOOD_SELECTED_ID = 8;
	private static final int		SYMPTOMS		= 9, SYMPTOMS_ID = 10;
	private static final int		SYMPTOMS_SELECTED	= 11, SYMPTOMS_SELECTED_ID = 12;

	private static final int		MEDICINE			= 13, MEDICINE_ID = 14;
	private static final int		USER_PROFILE		= 15, USER_PROFILE_ID = 16;
	private static final int		APPLICATION_SETTINGS	= 17, APPLICATION_SETTINGS_ID = 18;
	private static final int		APPLICATION_CONSTANTS	= 19, APPLICATION_CONSTANTS_ID = 20;
	private static final int		SKINS					= 21, SKINS_ID = 22;
	private static final int		NOTIFICATIONS			= 23, NOTIFICATIONS_ID = 24;

	private static final UriMatcher	URI_MATCHER;
	static
	{
		// Create and initialize URI matcher.
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_APPLICATION_CONSTANTS, APPLICATION_CONSTANTS);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_APPLICATION_CONSTANTS + "/#", APPLICATION_CONSTANTS_ID);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_APPLICATION_SETTINGS, APPLICATION_SETTINGS);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_APPLICATION_SETTINGS + "/#", APPLICATION_SETTINGS_ID);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_TRACKER, PERIOD_TRACKER);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_TRACKER + "/#", PERIOD_TRACKER_ID);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_DAY_DETAILS, DAY_DETAILS);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_DAY_DETAILS + "/#", DAY_DETAILS_ID);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_MOOD, MOOD);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_MOOD + "/#", MOOD_ID);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_MOOD_SELECTED, MOOD_SELECTED);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_MOOD_SELECTED + "/*", MOOD_SELECTED_ID);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_SYMPTOMS, SYMPTOMS);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_SYMPTOMS + "/#", SYMPTOMS_ID);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_SYMPTOMS_SELECTED, SYMPTOMS_SELECTED);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_SYMPTOMS_SELECTED + "/#", SYMPTOMS_SELECTED_ID);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_MEDICINE, MEDICINE);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_MEDICINE + "/#", MEDICINE_ID);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_USER_PROFILE, USER_PROFILE);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_USER_PROFILE + "/#", USER_PROFILE_ID);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_SKINS, SKINS);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_SKINS + "/#", SKINS_ID);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_NOTIFICATIONS, NOTIFICATIONS);
		URI_MATCHER.addURI(DBManager.AUTHORITY, DBManager.TABLE_PERIOD_NOTIFICATIONS + "/*", NOTIFICATIONS_ID);
	}

	@Override
	public boolean onCreate()
	{
		mOpenHelper = new PeriodTrackerDBHandler(getContext());
		// Assumes that any failures will be reported by a thrown exception.
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		 SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	        String finalSortOrder = sortOrder;
	        String[] finalSelectionArgs = selectionArgs;
	        String finalGrouping = null;
	        String finalHaving = null;
	        int type = URI_MATCHER.match(uri);
	        
	        Uri regUri = uri;
	        
	        // Security check to avoid data retrieval from outside
	        int remoteUid = Binder.getCallingUid();
	        int selfUid = android.os.Process.myUid();
	       if(remoteUid != selfUid) {
		        if (type == USER_PROFILE || type == USER_PROFILE_ID) {
					for(String proj : projection) {
		        		if(proj.toLowerCase().contains(DBProjections.UP_PASSWORD) || proj.toLowerCase().contains("*")) {
		        			throw new SecurityException("Password not readable from external apps");
		        		}
		        	}
				}
	        }
	        // Security check to avoid project of invalid fields or lazy projection
	       
	        List<String> possibles = getPossibleFieldsForType(type);//todokk
	        if(possibles == null) {
	            throw new SecurityException("You are asking wrong values " + type);
	        }
	        checkProjection(possibles, projection);
	        checkSelection(possibles, selection);

	    	Cursor c;
	    	long id;
	        switch (type) {
	            case PERIOD_TRACKER:
	                qb.setTables(DBManager.TABLE_PERIOD_TRACKER);
	                if(sortOrder == null) {
	                	finalSortOrder = DBProjections.ID + " ASC";
	                }
	                break;
	            case PERIOD_TRACKER_ID:
	            	 qb.setTables(DBManager.TABLE_PERIOD_TRACKER);
	                qb.appendWhere(DBProjections.ID + "=?");
	                finalSelectionArgs = DatabaseUtilsCompat.appendSelectionArgs(selectionArgs, new String[] { uri.getLastPathSegment() });
	                break;
	            case DAY_DETAILS:
	                qb.setTables(DBManager.TABLE_PERIOD_DAY_DETAILS);
	                if(sortOrder == null) {
	                	finalSortOrder = DBProjections.DD_DATE + " DESC";
	                }
	                break;
	            case DAY_DETAILS_ID:
	                qb.setTables(DBManager.TABLE_PERIOD_DAY_DETAILS);
	                qb.appendWhere(DBProjections.DD_DATE + "=?");
	                finalSelectionArgs = DatabaseUtilsCompat.appendSelectionArgs(selectionArgs, new String[] { uri.getLastPathSegment() });
	                break;
	            case MOOD:
	                qb.setTables(DBManager.TABLE_PERIOD_MOOD);
	                if(sortOrder == null) {
	                	finalSortOrder =DBProjections.ID;
	                }
	                break;
	            case MOOD_ID:
	                qb.setTables(DBManager.TABLE_PERIOD_MOOD);
	                qb.appendWhere(DBProjections.ID + "=?");
	                finalSelectionArgs = DatabaseUtilsCompat.appendSelectionArgs(selectionArgs, new String[] { uri.getLastPathSegment() });
	                break;
	            case MOOD_SELECTED:
	                qb.setTables(DBManager.TABLE_PERIOD_MOOD_SELECTED);
	                if(sortOrder == null) {
	                    finalSortOrder = DBProjections.MOOD_ID + " DESC";
	                }
	                break;
	            case MOOD_SELECTED_ID:
	                qb.setTables(DBManager.TABLE_PERIOD_MOOD_SELECTED);
	                qb.appendWhere(DBProjections.MOOD_ID + "=?");
	                finalSelectionArgs = DatabaseUtilsCompat.appendSelectionArgs(selectionArgs, new String[] { uri.getLastPathSegment() });
	                break;
	          /*  case THREADS:
	                qb.setTables(DBManager.TABLE_PERIOD_MOOD_SELECTED);
	                if(sortOrder == null) {
	                    finalSortOrder = SipMessage.FIELD_DATE + " DESC";
	                }
	                projection = new String[]{
	                    "ROWID AS _id",
	                    SipMessage.FIELD_FROM, 
	                    SipMessage.FIELD_FROM_FULL, 
	                    SipMessage.FIELD_TO, 
	                    "CASE " + 
	                            "WHEN " + SipMessage.FIELD_FROM + "='SELF' THEN "
	                                + SipMessage.FIELD_TO + 
	                            " WHEN " + SipMessage.FIELD_FROM + "!='SELF' THEN " 
	                                + SipMessage.FIELD_FROM
	                    + " END AS message_ordering",
	                    SipMessage.FIELD_BODY, 
	                    "MAX(" + SipMessage.FIELD_DATE + ") AS " + SipMessage.FIELD_DATE,
	                    "MIN(" + SipMessage.FIELD_READ + ") AS " + SipMessage.FIELD_READ,
	                    //SipMessage.FIELD_READ,
	                    "COUNT(" + SipMessage.FIELD_DATE + ") AS counter"
	                };
	                //qb.appendWhere(SipMessage.FIELD_TYPE + " in (" + SipMessage.MESSAGE_TYPE_INBOX
	                //        + "," + SipMessage.MESSAGE_TYPE_SENT + ")");
	                finalGrouping = "message_ordering";
	                regUri = SipMessage.MESSAGE_URI;
	                break;
	            case THREADS_ID:
	                qb.setTables(SipMessage.MESSAGES_TABLE_NAME);
	                if(sortOrder == null) {
	                    finalSortOrder = SipMessage.FIELD_DATE + " DESC";
	                }
	                projection = new String[]{
	                        "ROWID AS _id",
	                        SipMessage.FIELD_FROM, 
	                        SipMessage.FIELD_TO, 
	                        SipMessage.FIELD_BODY, 
	                        SipMessage.FIELD_DATE, 
	                        SipMessage.FIELD_MIME_TYPE,
	                        SipMessage.FIELD_TYPE,
	                        SipMessage.FIELD_STATUS,
	                        SipMessage.FIELD_FROM_FULL
	                    };
	                qb.appendWhere(MESSAGES_THREAD_SELECTION);
	                String from = uri.getLastPathSegment();
	                finalSelectionArgs = DatabaseUtilsCompat.appendSelectionArgs(selectionArgs, new String[] { from, from });
	                regUri = SipMessage.MESSAGE_URI;
	                break;*/
	           /* case SYMPTOMS:
	            	synchronized (profilesStatus) {
	            		ContentValues[] cvs = new ContentValues[profilesStatus.size()];
	            		int i = 0;
	            		for(ContentValues  ps : profilesStatus.values()) {
	            			cvs[i] = ps;
	            			i++;
	            		}
	            		c = getCursor(cvs);
					}
	            	if(c != null) {
	            		c.setNotificationUri(getContext().getContentResolver(), uri);
	            	}
	                return c;
	            case ACCOUNTS_STATUS_ID:
	            	id = ContentUris.parseId(uri);
	            	synchronized (profilesStatus) {
	            		ContentValues cv = profilesStatus.get(id);
	            		if(cv == null) {
	            			return null;
	            		}
	            		c = getCursor(new ContentValues[] {cv});
					}
	                c.setNotificationUri(getContext().getContentResolver(), uri);
	                return c;*/
	            default:
	                throw new IllegalArgumentException(UNKNOWN_URI_LOG + uri);
	        }

	        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

	        c = qb.query(db, projection, selection, finalSelectionArgs,
	                finalGrouping, finalHaving, finalSortOrder);

	        c.setNotificationUri(getContext().getContentResolver(), regUri);
	        return c;
	}

	@Override
	public String getType(Uri uri)
	{
		switch (URI_MATCHER.match(uri)) {
            case PERIOD_TRACKER:
                return DBManager.PERIOD_TRACKER_CONTENT_TYPE;
            case PERIOD_TRACKER_ID:
                return DBManager.PERIOD_TRACKER_CONTENT_ITEM_TYPE;
            case DAY_DETAILS:
            	return DBManager.DAY_DETAILS_CONTENT_TYPE;
            case DAY_DETAILS_ID:
            	return DBManager.DAY_DETAILS_CONTENT_TYPE;
            case MOOD :
            	return DBManager.MOOD_CONTENT_TYPE;
            case MOOD_ID :
            	return DBManager.MOOD_CONTENT_ITEM_TYPE;
            case MOOD_SELECTED:
            	return DBManager.MOOD_SELECTED_CONTENT_TYPE;
            case MOOD_SELECTED_ID:
            	return DBManager.MOOD_SELECTED_CONTENT_ITEM_TYPE            			;
            case SYMPTOMS:
                return DBManager.SYMPTIOMS_CONTENT_TYPE;
            case SYMPTOMS_ID:
                return DBManager.SYMPTIOMS_CONTENT_ITEM_TYPE;
            case SYMPTOMS_SELECTED:
                return DBManager.SYMPTIOMS_SELECTED_CONTENT_TYPE;
            case SYMPTOMS_SELECTED_ID:
                return DBManager.SYMPTIOMS_SELECTED_CONTENT_ITEM_TYPE;               
                
            case MEDICINE:
                return DBManager.MEDICINE_CONTENT_TYPE;
            case MEDICINE_ID:
                return DBManager.MEDICINE_CONTENT_ITEM_TYPE;
            case USER_PROFILE:
            	return DBManager.USER_PROFILE_CONTENT_TYPE;
            case USER_PROFILE_ID:
            	return DBManager.USER_PROFILE_CONTENT_ITEM_TYPE;
            case APPLICATION_CONSTANTS :
            	return DBManager.SKINS_CONTENT_TYPE;
            case APPLICATION_CONSTANTS_ID :
            	return DBManager.SKINS_CONTENT_ITEM_TYPE;
            case APPLICATION_SETTINGS:
            	return DBManager.NOTIFICATIONS_CONTENT_TYPE;
            case APPLICATION_SETTINGS_ID:
            	return DBManager.NOTIFICATIONS_CONTENT_ITEM_TYPE;
            case SKINS:
                return DBManager.APPLICATION_CONSTANTS_CONTENT_TYPE;
            case SKINS_ID:
                return DBManager.APPLICATION_CONSTANTS_CONTENT_ITEM_TYPE;
            case NOTIFICATIONS:
                return DBManager.APPLICATION_SETTINGS_CONTENT_TYPE;
            case NOTIFICATIONS_ID:
                return DBManager.APPLICATION_SETTINGS_CONTENT_ITEM_TYPE;
                
            default:
                throw new IllegalArgumentException(UNKNOWN_URI_LOG + uri);
        }
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	private static List<String> getPossibleFieldsForType(int type)
	{
        List<String> possibles = null;
      switch (type) {
            case PERIOD_TRACKER:
            case PERIOD_TRACKER_ID:
                possibles = Arrays.asList(DBProjections.PERIOD_TRACKER_FULL_PROJECTION);
                break;
            case DAY_DETAILS:
            case DAY_DETAILS_ID:
                possibles = Arrays.asList(DBProjections.DAY_DETAILS_FULL_PROJECTION);
                break;
                /*          case MOOD:
            case MOOD_ID:
                possibles = Arrays.asList(FILTERS_FULL_PROJECTION);
                break;
            case MOOD_SELECTED:
            case MOOD_SELECTED_ID:
                possibles = Arrays.asList(MESSAGES_FULL_PROJECTION);
                break;
            case SYMPTOMS:
            case SYMPTOMS_ID:
                possibles = new ArrayList<String>();
                break;
            case MOOD_SELECTED:
            case MOOD_SELECTED_ID:
                possibles = new ArrayList<String>();
            case MEDICINE:
            case MEDICINE_ID:
                possibles = Arrays.asList(ACCOUNT_FULL_PROJECTION);
                break;
            case SKINS:
            case SKINS_ID:
                possibles = Arrays.asList(CALL_LOG_FULL_PROJECTION);
                break;
            case NOTIFICATIONS:
            case NOTIFICATIONS_ID:
                possibles = Arrays.asList(FILTERS_FULL_PROJECTION);
                break;
            case USER_PROFILE:
            case USER_PROFILE_ID:
                possibles = Arrays.asList(MESSAGES_FULL_PROJECTION);
                break;
            case APPLICATION_CONSTANTS:
            case APPLICATION_CONSTANTS_ID:
                possibles = new ArrayList<String>();
                break;
            case APPLICATION_SETTINGS:
            case APPLICATION_SETTINGS_ID:
                possibles = new ArrayList<String>();*/
            default:
        }
        return possibles;
	}
	
	private static void checkSelection(List<String> possibles, String selection) {
        if(selection != null) {
            String cleanSelection = selection.toLowerCase();
            for(String field : possibles) {
                cleanSelection = cleanSelection.replace(field, "");
            }
            cleanSelection = cleanSelection.replaceAll(" in \\([0-9 ,]+\\)", "");
            cleanSelection = cleanSelection.replaceAll(" and ", "");
            cleanSelection = cleanSelection.replaceAll(" or ", "");
            cleanSelection = cleanSelection.replaceAll("[0-9]+", "");
            cleanSelection = cleanSelection.replaceAll("[=? ]", "");
            if(cleanSelection.length() > 0) {
                throw new SecurityException("You are selecting wrong thing " + cleanSelection);
            }
        }
	}
	
	private static void checkProjection(List<String> possibles, String[] projection) {
        if(projection != null) {
            // Ensure projection is valid
            for(String proj : projection) {
                proj = proj.replaceAll(" AS [a-zA-Z0-9_]+$", "");
                if(!possibles.contains(proj)) {
                    throw new SecurityException("You are asking wrong values " + proj);
                }
            }
        }
	}

}
