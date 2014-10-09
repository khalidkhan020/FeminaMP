package com.linchpin.periodtracker.utlity;

import java.util.HashMap;
import java.util.Map;

public class ConstantsKey 
{

	public static final String sceretKey = "nv93h50sk1zh508v";
	Map<String, String> map;
	public static final String IN_APP_ID = "mpt_ad_free";
	public static final String VersionKey = "V";
	// Keys For Period TrackTable;
	public static final String PERIOD_TRACK = "PERIOD_TRACK";
	public static final String ST_D = "ST_D";
	public static final String EN_D = "EN_D";
	public static final String PT_PL = "PT_PL";
	public static final String PT_CL = "PT_CL";
	public static final String PT_PM = "PT_PM";
	public static final String PT_PSM = "PT_PSM";

	// keys For Mood_Selecetd
	public static final String Mood_Selected = "Mood_Selected";
	public static final String MS_MID = "MS_MID";
	public static final String MS_DDID = "MS_DDID";
	public static final String MS_MW = "MS_MW";

	// keys For Symtom_Selecetd

	public static final String SYMTOM_SELECTED = "SYMTOM_SELECTED";
	public static final String SS_SID = "SS_SID";
	public static final String SS_DDID = "SS_DDID";
	public static final String SS_SW = "SS_SW";

	// keys For Medicine

	public static final String Medicine = "Medicine";
	public static final String MDN = "MDN";
	public static final String MDQ = "MDQ";
	public static final String M_DD = "M_DD";

	// keys For User_Profile

	public static final String User_Profile = "User_Profile_V";
	public static final String FN = "FN";
	public static final String LN = "LN";
	public static final String UN = "UN";
	public static final String US_PP = "US_PP";

	// keys for Application_Settings

	public static final String Application_Settings = "Application_Settings";
	public static final String DPl = "DPl";
	public static final String DCL = "DCL";
	public static final String DOL = "DOL";
	public static final String DWU = "DWU";
	public static final String DTU = "DTU";
	public static final String DHU = "DHU";
	public static final String DAL = "DAL";
	public static final String DDF = "DDF";
	public static final String DPM = "DPM";
	public static final String PSQ = "PSQ";
	public static final String PSA = "PSA";
	public static final String DAP = "DAP";
	public static final String DWV = "DWV";
	public static final String DHV = "DHV";
	public static final String DTV = "DTV";
	public static final String DPMF = "DPMF";
	public static final String PED = "PED";
	public static final String PSN = "PSN";
	public static final String FSN = "FSN";
	public static final String OSN = "OSN";
	public static final String PSNM = "PSNM";
	public static final String FSNM = "FSNM";
	public static final String OSNM = "OSNM";
	public static final String DOW = "DOW";
	public static final String DAVGL = "DAVGL";

	// keys for Day_Detail Table

	public static final String Day_Detail = "Day_Detail";
	public static final String DD_DT = "DD_DT";
	public static final String DD_NT = "DD_NT";
	public static final String DD_WV = "DD_WV";
	public static final String DD_PR = "DD_PR";
	public static final String DD_HV = "DD_HV";
	public static final String DD_TV = "DD_TV";
	public static final String DD_IV = "DD_IV";
	public static final String DD_PV = "DD_PV";

	public ConstantsKey(String version) {
		if (version.equals("1.0"))
			map = getMapForVersion1_0();

	}

	public String getKey(String key) {
		String string = null;
		if (map == null) {
			map = getMapForVersion1_0();
		}
		string = map.get(key);
		return string;

	}

	public Map<String, String> getMapForVersion1_0() {
		Map<String, String> map1 = new HashMap<String, String>();

		map1.put(User_Profile, "A");
		map1.put(FN, "A_A");
		map1.put(LN, "A_B");
		map1.put(UN, "A_C");
		map1.put(US_PP, "A_D");

		map1.put(Application_Settings, "B");
		map1.put(DPl, "B_A");
		map1.put(DCL, "B_B");
		map1.put(DOL, "B_C");
		map1.put(DWU, "B_D");
		map1.put(DTU, "B_E");
		map1.put(DHU, "B_F");
		map1.put(DAL, "B_G");
		map1.put(DDF, "B_H");
		map1.put(DPM, "B_I");
		map1.put(PSQ, "B_J");
		map1.put(PSA, "B_K");
		map1.put(DAP, "B_L");
		map1.put(DHV, "B_M");
		map1.put(DWV, "B_N");
		map1.put(DTV, "B_O");
		map1.put(DPMF, "B_P");
		map1.put(PED, "B_Q");
		map1.put(PSN, "B_R");
		map1.put(FSN, "B_S");
		map1.put(OSN, "B_T");
		map1.put(PSNM, "B_U");
		map1.put(FSNM, "B_V");
		map1.put(OSNM, "B_W");
		map1.put(DOW, "B_X");
		map1.put(DAVGL, "B_Y");

		map1.put(PERIOD_TRACK, "C");
		map1.put(ST_D, "C_A");
		map1.put(EN_D, "C_B");
		map1.put(PT_PL, "C_C");
		map1.put(PT_CL, "C_D");
		map1.put(PT_PM, "C_E");
		map1.put(PT_PSM, "C_F");

		map1.put(Day_Detail, "D");
		map1.put(DD_DT, "D_A");
		map1.put(DD_NT, "D_B");
		map1.put(DD_WV, "D_C");
		map1.put(DD_HV, "D_D");
		map1.put(DD_TV, "D_E");
		map1.put(DD_IV, "D_F");
		map1.put(DD_PV, "D_G");
		map1.put(DD_PR, "D_K");
		
		map1.put(Mood_Selected, "D_H");
		map1.put(MS_MID, "D_H_A");
		map1.put(MS_DDID, "D_H_B");
		map1.put(MS_MW, "D_H_C");

		map1.put(SYMTOM_SELECTED, "D_I");
		map1.put(SS_SID, "D_I_A");
		map1.put(SS_DDID, "D_I_B");
		map1.put(SS_SW, "D_I_C");

		map1.put(Medicine, "D_J");
		map1.put(MDN, "D_J_A");
		map1.put(MDQ, "D_J_B");
		map1.put(M_DD, "D_J_C");

		return map1;

	}

}
