// Copyright © 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


// http://en.wikipedia.org/wiki/ISO_3166-1
// http://en.wikipedia.org/wiki/ISO_3166-2
public class ISO3166
{
	private static CMap<String,String> countries = new CMap();
	static
	{
		add("AF", TXT.get("ISO3166.country.af.Afghanistan", "Afghanistan")); 
		add("AX", TXT.get("ISO3166.country.ax.Aland Islands", "Åland Islands")); 
		add("AL", TXT.get("ISO3166.country.al.Albania", "Albania")); 
		add("DZ", TXT.get("ISO3166.country.dz.Algeria", "Algeria")); 
		add("AS", TXT.get("ISO3166.country.as.American Samoa", "American Samoa"));  
		add("AD", TXT.get("ISO3166.country.ad.Andorra", "Andorra")); 
		add("AO", TXT.get("ISO3166.country.ao.Angola", "Angola")); 
		add("AI", TXT.get("ISO3166.country.ai.Anguilla", "Anguilla")); 
		add("AQ", TXT.get("ISO3166.country.aq.Antarctica", "Antarctica")); 
		add("AG", TXT.get("ISO3166.country.ag.Antigua and Barbuda", "Antigua and Barbuda"));  
		add("AR", TXT.get("ISO3166.country.ar.Argentina", "Argentina")); 
		add("AM", TXT.get("ISO3166.country.am.Armenia", "Armenia")); 
		add("AW", TXT.get("ISO3166.country.aw.Aruba", "Aruba")); 
		add("AU", TXT.get("ISO3166.country.au.Australia", "Australia"));  
		add("AT", TXT.get("ISO3166.country.at.Austria", "Austria"));  
		add("AZ", TXT.get("ISO3166.country.az.Azerbaijan", "Azerbaijan"));  
		add("BS", TXT.get("ISO3166.country.bs.Bahamas", "Bahamas"));  
		add("BH", TXT.get("ISO3166.country.bh.Bahrain", "Bahrain"));  
		add("BD", TXT.get("ISO3166.country.bd.Bangladesh", "Bangladesh"));  
		add("BB", TXT.get("ISO3166.country.bb.Barbados", "Barbados"));  
		add("BY", TXT.get("ISO3166.country.by.Belarus", "Belarus"));  
		add("BE", TXT.get("ISO3166.country.be.Belgium", "Belgium"));  
		add("BZ", TXT.get("ISO3166.country.bz.Belize", "Belize"));  
		add("BJ", TXT.get("ISO3166.country.bj.Benin", "Benin"));  
		add("BM", TXT.get("ISO3166.country.bm.Bermuda", "Bermuda"));  
		add("BT", TXT.get("ISO3166.country.bt.Bhutan", "Bhutan"));  
		add("BO", TXT.get("ISO3166.country.bo.Bolivia", "Bolivia"));  
		add("BA", TXT.get("ISO3166.country.ba.Bosnia and Herzegovina", "Bosnia and Herzegovina"));  
		add("BW", TXT.get("ISO3166.country.bw.Botswana", "Botswana"));  
		add("BV", TXT.get("ISO3166.country.bv.Bouvet Island", "Bouvet Island"));  
		add("BR", TXT.get("ISO3166.country.br.Brazil", "Brazil"));  
		add("IO", TXT.get("ISO3166.country.io.British Indian Ocean Territory", "British Indian Ocean Territory"));  
		add("BN", TXT.get("ISO3166.country.bn.Brunei Darussalam", "Brunei"));  
		add("BG", TXT.get("ISO3166.country.bg.Bulgaria", "Bulgaria"));  
		add("BF", TXT.get("ISO3166.country.bf.Burkina Faso", "Burkina Faso"));  
		add("BI", TXT.get("ISO3166.country.bi.Burundi", "Burundi"));  
		add("KH", TXT.get("ISO3166.country.kh.Cambodia", "Cambodia"));  
		add("CM", TXT.get("ISO3166.country.cm.Cameroon", "Cameroon"));  
		add("CA", TXT.get("ISO3166.country.ca.Canada", "Canada"));  
		add("CV", TXT.get("ISO3166.country.cv.Cape Verde", "Cape Verde"));  
		add("KY", TXT.get("ISO3166.country.ky.Cayman Islands", "Cayman Islands"));  
		add("CF", TXT.get("ISO3166.country.cf.Central African Republic", "Central African Republic"));  
		add("TD", TXT.get("ISO3166.country.td.Chad", "Chad"));  
		add("CL", TXT.get("ISO3166.country.cl.Chile", "Chile"));  
		add("CN", TXT.get("ISO3166.country.cn.China", "China"));  
		add("CX", TXT.get("ISO3166.country.cx.Christmas Island", "Christmas Island"));  
		add("CC", TXT.get("ISO3166.country.cc.Cocos (Keeling) Islands", "Cocos Islands"));  
		add("CO", TXT.get("ISO3166.country.co.Colombia", "Colombia"));  
		add("KM", TXT.get("ISO3166.country.km.Comoros", "Comoros"));  
		add("CG", TXT.get("ISO3166.country.cg.Congo", "Congo"));  
		add("CD", TXT.get("ISO3166.country.cd.Congo, the Democratic Republic of the", "DR Congo"));  
		add("CK", TXT.get("ISO3166.country.ck.Cook Islands", "Cook Islands"));  
		add("CR", TXT.get("ISO3166.country.cr.Costa Rica", "Costa Rica"));  
		add("CI", TXT.get("ISO3166.country.ci.Cote d'Ivoire", "Côte d'Ivoire"));  
		add("HR", TXT.get("ISO3166.country.hr.Croatia", "Croatia"));  
		add("CU", TXT.get("ISO3166.country.cu.Cuba", "Cuba"));  
		add("CY", TXT.get("ISO3166.country.cy.Cyprus", "Cyprus"));  
		add("CZ", TXT.get("ISO3166.country.cz.Czech Republic", "Czech Republic"));  
		add("DK", TXT.get("ISO3166.country.dk.Denmark", "Denmark"));  
		add("DJ", TXT.get("ISO3166.country.dj.Djibouti", "Djibouti"));  
		add("DM", TXT.get("ISO3166.country.dm.Dominica", "Dominica"));  
		add("DO", TXT.get("ISO3166.country.do.Dominican Republic", "Dominican Republic"));  
		add("EC", TXT.get("ISO3166.country.ec.Ecuador", "Ecuador"));  
		add("EG", TXT.get("ISO3166.country.eg.Egypt", "Egypt"));  
		add("SV", TXT.get("ISO3166.country.sv.El Salvador", "El Salvador"));  
		add("GQ", TXT.get("ISO3166.country.gq.Equatorial Guinea", "Equatorial Guinea"));  
		add("ER", TXT.get("ISO3166.country.er.Eritrea", "Eritrea"));  
		add("EE", TXT.get("ISO3166.country.ee.Estonia", "Estonia"));  
		add("ET", TXT.get("ISO3166.country.et.Ethiopia", "Ethiopia"));  
		add("FK", TXT.get("ISO3166.country.fk.Falkland Islands (Malvinas)", "Falkland Islands"));  
		add("FO", TXT.get("ISO3166.country.fo.Faroe Islands", "Faroe Islands"));  
		add("FJ", TXT.get("ISO3166.country.fj.Fiji", "Fiji"));  
		add("FI", TXT.get("ISO3166.country.fi.Finland", "Finland"));  
		add("FR", TXT.get("ISO3166.country.fr.France", "France"));  
		add("GF", TXT.get("ISO3166.country.gf.Guiana", "Guiana"));  
		add("PF", TXT.get("ISO3166.country.pf.French Polynesia", "French Polynesia"));  
		add("TF", TXT.get("ISO3166.country.tf.French Southern Territories", "French Southern Territories"));  
		add("GA", TXT.get("ISO3166.country.ga.Gabon", "Gabon"));  
		add("GM", TXT.get("ISO3166.country.gm.Gambia", "Gambia"));  
		add("GE", TXT.get("ISO3166.country.ge.Georgia", "Georgia"));  
		add("DE", TXT.get("ISO3166.country.de.Germany", "Germany"));  
		add("GH", TXT.get("ISO3166.country.gh.Ghana", "Ghana"));  
		add("GI", TXT.get("ISO3166.country.gi.Gibraltar", "Gibraltar"));  
		add("GR", TXT.get("ISO3166.country.gr.Greece", "Greece"));  
		add("GL", TXT.get("ISO3166.country.gl.Greenland", "Greenland"));  
		add("GD", TXT.get("ISO3166.country.gd.Grenada", "Grenada"));  
		add("GP", TXT.get("ISO3166.country.gp.Guadeloupe", "Guadeloupe"));  
		add("GU", TXT.get("ISO3166.country.gu.Guam", "Guam"));  
		add("GT", TXT.get("ISO3166.country.gt.Guatemala", "Guatemala"));  
		add("GG", TXT.get("ISO3166.country.gg.Guernsey", "Guernsey"));  
		add("GN", TXT.get("ISO3166.country.gn.Guinea", "Guinea"));  
		add("GW", TXT.get("ISO3166.country.gw.Guinea-Bissau", "Guinea-Bissau"));  
		add("GY", TXT.get("ISO3166.country.gy.Guyana", "Guyana"));  
		add("HT", TXT.get("ISO3166.country.ht.Haiti", "Haiti"));  
		add("HM", TXT.get("ISO3166.country.hm.Heard Island and McDonald Islands", "Heard Island and McDonald Islands")); 
		add("VA", TXT.get("ISO3166.country.va.Holy See (Vatican City State)", "Vatican")); 
		add("HN", TXT.get("ISO3166.country.hn.Honduras", "Honduras")); 
		add("HK", TXT.get("ISO3166.country.hk.Hong Kong", "Hong Kong")); 
		add("HU", TXT.get("ISO3166.country.hu.Hungary", "Hungary")); 
		add("IS", TXT.get("ISO3166.country.is.Iceland", "Iceland")); 
		add("IN", TXT.get("ISO3166.country.in.India", "India")); 
		add("ID", TXT.get("ISO3166.country.id.Indonesia", "Indonesia")); 
		add("IR", TXT.get("ISO3166.country.ir.Iran, Islamic Republic of", "Iran"));  
		add("IQ", TXT.get("ISO3166.country.iq.Iraq", "Iraq")); 
		add("IE", TXT.get("ISO3166.country.ie.Ireland", "Ireland")); 
		add("IM", TXT.get("ISO3166.country.im.Isle of Man", "Isle of Man")); 
		add("IL", TXT.get("ISO3166.country.il.Israel", "Israel")); 
		add("IT", TXT.get("ISO3166.country.it.Italy", "Italy")); 
		add("JM", TXT.get("ISO3166.country.jm.Jamaica", "Jamaica")); 
		add("JP", TXT.get("ISO3166.country.jp.Japan", "Japan")); 
		add("JE", TXT.get("ISO3166.country.je.Jersey", "Jersey")); 
		add("JO", TXT.get("ISO3166.country.jo.Jordan", "Jordan")); 
		add("KZ", TXT.get("ISO3166.country.kz.Kazakhstan", "Kazakhstan")); 
		add("KE", TXT.get("ISO3166.country.ke.Kenya", "Kenya")); 
		add("KI", TXT.get("ISO3166.country.ki.Kiribati", "Kiribati")); 
		add("KP", TXT.get("ISO3166.country.kp.Korea, Democratic People's Republic of", "North Korea"));  
		add("KR", TXT.get("ISO3166.country.kr.Korea, Republic of", "South Korea")); 
		add("KW", TXT.get("ISO3166.country.kw.Kuwait", "Kuwait")); 
		add("KG", TXT.get("ISO3166.country.kg.Kyrgyzstan", "Kyrgyzstan")); 
		add("LA", TXT.get("ISO3166.country.la.Lao People's Democratic Republic", "Laos"));  
		add("LV", TXT.get("ISO3166.country.lv.Latvia", "Latvia")); 
		add("LB", TXT.get("ISO3166.country.lb.Lebanon", "Lebanon")); 
		add("LS", TXT.get("ISO3166.country.ls.Lesotho", "Lesotho")); 
		add("LR", TXT.get("ISO3166.country.lr.Liberia", "Liberia")); 
		add("LY", TXT.get("ISO3166.country.ly.Libyan Arab Jamahiriya", "Libya"));  
		add("LI", TXT.get("ISO3166.country.li.Liechtenstein", "Liechtenstein")); 
		add("LT", TXT.get("ISO3166.country.lt.Lithuania", "Lithuania")); 
		add("LU", TXT.get("ISO3166.country.lu.Luxembourg", "Luxembourg")); 
		add("MO", TXT.get("ISO3166.country.mo.Macao", "Macao")); 
		add("MK", TXT.get("ISO3166.country.mk.Macedonia, the former Yugoslav Republic of", "Macedonia"));  
		add("MG", TXT.get("ISO3166.country.mg.Madagascar", "Madagascar")); 
		add("MW", TXT.get("ISO3166.country.mw.Malawi", "Malawi")); 
		add("MY", TXT.get("ISO3166.country.my.Malaysia", "Malaysia")); 
		add("MV", TXT.get("ISO3166.country.mv.Maldives", "Maldives")); 
		add("ML", TXT.get("ISO3166.country.ml.Mali", "Mali")); 
		add("MT", TXT.get("ISO3166.country.mt.Malta", "Malta")); 
		add("MH", TXT.get("ISO3166.country.mh.Marshall Islands", "Marshall Islands"));  
		add("MQ", TXT.get("ISO3166.country.mq.Martinique", "Martinique"));  
		add("MR", TXT.get("ISO3166.country.mr.Mauritania", "Mauritania"));  
		add("MU", TXT.get("ISO3166.country.mu.Mauritius", "Mauritius"));  
		add("YT", TXT.get("ISO3166.country.yt.Mayotte", "Mayotte"));  
		add("MX", TXT.get("ISO3166.country.mx.Mexico", "Mexico"));  
		add("FM", TXT.get("ISO3166.country.fm.Micronesia, Federated States of", "Micronesia"));  
		add("MD", TXT.get("ISO3166.country.md.Moldova, Republic of", "Moldova"));  
		add("MC", TXT.get("ISO3166.country.mc.Monaco", "Monaco"));  
		add("MN", TXT.get("ISO3166.country.mn.Mongolia", "Mongolia"));  
		add("ME", TXT.get("ISO3166.country.me.Montenegro", "Montenegro"));  
		add("MS", TXT.get("ISO3166.country.ms.Montserrat", "Montserrat"));  
		add("MA", TXT.get("ISO3166.country.ma.Morocco", "Morocco"));  
		add("MZ", TXT.get("ISO3166.country.mz.Mozambique", "Mozambique"));  
		add("MM", TXT.get("ISO3166.country.mm.Myanmar", "Myanmar"));  
		add("NA", TXT.get("ISO3166.country.na.Namibia", "Namibia"));  
		add("NR", TXT.get("ISO3166.country.nr.Nauru", "Nauru"));  
		add("NP", TXT.get("ISO3166.country.np.Nepal", "Nepal"));  
		add("NL", TXT.get("ISO3166.country.nl.Netherlands", "Netherlands"));  
		add("AN", TXT.get("ISO3166.country.an.Netherlands Antilles", "Netherlands Antilles"));  
		add("NC", TXT.get("ISO3166.country.nc.New Caledonia", "New Caledonia"));  
		add("NZ", TXT.get("ISO3166.country.nz.New Zealand", "New Zealand"));  
		add("NI", TXT.get("ISO3166.country.ni.Nicaragua", "Nicaragua"));  
		add("NE", TXT.get("ISO3166.country.ne.Niger", "Niger"));  
		add("NG", TXT.get("ISO3166.country.ng.Nigeria", "Nigeria"));  
		add("NU", TXT.get("ISO3166.country.nu.Niue", "Niue"));  
		add("NF", TXT.get("ISO3166.country.nf.Norfolk Island", "Norfolk Island"));  
		add("MP", TXT.get("ISO3166.country.mp.Northern Mariana Islands", "Northern Mariana Islands"));  
		add("NO", TXT.get("ISO3166.country.no.Norway", "Norway"));  
		add("OM", TXT.get("ISO3166.country.om.Oman", "Oman"));  
		add("PK", TXT.get("ISO3166.country.pk.Pakistan", "Pakistan"));  
		add("PW", TXT.get("ISO3166.country.pw.Palau", "Palau"));  
		add("PS", TXT.get("ISO3166.country.ps.Palestine", "Palestine"));  
		add("PA", TXT.get("ISO3166.country.pa.Panama", "Panama"));  
		add("PG", TXT.get("ISO3166.country.pg.Papua New Guinea", "Papua New Guinea"));  
		add("PY", TXT.get("ISO3166.country.py.Paraguay", "Paraguay"));  
		add("PE", TXT.get("ISO3166.country.pe.Peru", "Peru"));  
		add("PH", TXT.get("ISO3166.country.ph.Philippines", "Philippines"));  
		add("PN", TXT.get("ISO3166.country.pn.Pitcairn", "Pitcairn"));  
		add("PL", TXT.get("ISO3166.country.pl.Poland", "Poland"));  
		add("PT", TXT.get("ISO3166.country.pt.Portugal", "Portugal"));  
		add("PR", TXT.get("ISO3166.country.pr.Puerto Rico", "Puerto Rico"));  
		add("QA", TXT.get("ISO3166.country.qa.Qatar", "Qatar"));  
		add("RE", TXT.get("ISO3166.country.re.Reunion", "Réunion"));  
		add("RO", TXT.get("ISO3166.country.ro.Romania", "Romania"));  
		add("RU", TXT.get("ISO3166.country.ru.Russian Federation", "Russia"));  
		add("RW", TXT.get("ISO3166.country.rw.Rwanda", "Rwanda"));  
		add("BL", TXT.get("ISO3166.country.bl.Saint Barthelemy", "Saint Barthélemy"));  
		add("SH", TXT.get("ISO3166.country.sh.Saint Helena", "Saint Helena"));  
		add("KN", TXT.get("ISO3166.country.kn.Saint Kitts and Nevis", "Saint Kitts and Nevis"));  
		add("LC", TXT.get("ISO3166.country.lc.Saint Lucia", "Saint Lucia"));  
		add("MF", TXT.get("ISO3166.country.mf.Saint Martin (French part)", "Saint Martin"));  
		add("PM", TXT.get("ISO3166.country.pm.Saint Pierre and Miquelon", "Saint Pierre and Miquelon"));  
		add("VC", TXT.get("ISO3166.country.vc.Saint Vincent and the Grenadines", "Saint Vincent and the Grenadines"));  
		add("WS", TXT.get("ISO3166.country.ws.Samoa", "Samoa"));  
		add("SM", TXT.get("ISO3166.country.sm.San Marino", "San Marino"));  
		add("ST", TXT.get("ISO3166.country.st.Sao Tome and Principe", "Sao Tome and Principe"));  
		add("SA", TXT.get("ISO3166.country.sa.Saudi Arabia", "Saudi Arabia"));  
		add("SN", TXT.get("ISO3166.country.sn.Senegal", "Senegal"));  
		add("RS", TXT.get("ISO3166.country.rs.Serbia", "Serbia"));  
		add("SC", TXT.get("ISO3166.country.sc.Seychelles", "Seychelles"));  
		add("SL", TXT.get("ISO3166.country.sl.Sierra Leone", "Sierra Leone"));  
		add("SG", TXT.get("ISO3166.country.sg.Singapore", "Singapore"));  
		add("SK", TXT.get("ISO3166.country.sk.Slovakia", "Slovakia"));  
		add("SI", TXT.get("ISO3166.country.si.Slovenia", "Slovenia"));  
		add("SB", TXT.get("ISO3166.country.sb.Solomon Islands", "Solomon Islands"));  
		add("SO", TXT.get("ISO3166.country.so.Somalia", "Somalia"));  
		add("ZA", TXT.get("ISO3166.country.za.South Africa", "South Africa"));  
		add("GS", TXT.get("ISO3166.country.gs.South Georgia and the South Sandwich Islands", "South Georgia and the South Sandwich Islands"));  
		add("ES", TXT.get("ISO3166.country.es.Spain", "Spain"));  
		add("LK", TXT.get("ISO3166.country.lk.Sri Lanka", "Sri Lanka"));  
		add("SD", TXT.get("ISO3166.country.sd.Sudan", "Sudan"));  
		add("SR", TXT.get("ISO3166.country.sr.Suriname", "Suriname"));  
		add("SJ", TXT.get("ISO3166.country.sj.Svalbard and Jan Mayen", "Svalbard and Jan Mayen"));  
		add("SZ", TXT.get("ISO3166.country.sz.Swaziland", "Swaziland"));  
		add("SE", TXT.get("ISO3166.country.se.Sweden", "Sweden"));  
		add("CH", TXT.get("ISO3166.country.ch.Switzerland", "Switzerland"));  
		add("SY", TXT.get("ISO3166.country.sy.Syrian Arab Republic", "Syria"));  
		add("TW", TXT.get("ISO3166.country.tw.Taiwan", "Taiwan"));  
		add("TJ", TXT.get("ISO3166.country.tj.Tajikistan", "Tajikistan"));  
		add("TZ", TXT.get("ISO3166.country.tz.Tanzania, United Republic of", "Tanzania"));  
		add("TH", TXT.get("ISO3166.country.th.Thailand", "Thailand"));  
		add("TL", TXT.get("ISO3166.country.tl.Timor-Leste", "Timor-Leste"));  
		add("TG", TXT.get("ISO3166.country.tg.Togo", "Togo"));  
		add("TK", TXT.get("ISO3166.country.tk.Tokelau", "Tokelau"));  
		add("TO", TXT.get("ISO3166.country.to.Tonga", "Tonga"));  
		add("TT", TXT.get("ISO3166.country.tt.Trinidad and Tobago", "Trinidad and Tobago"));  
		add("TN", TXT.get("ISO3166.country.tn.Tunisia", "Tunisia"));  
		add("TR", TXT.get("ISO3166.country.tr.Turkey", "Turkey"));  
		add("TM", TXT.get("ISO3166.country.tm.Turkmenistan", "Turkmenistan"));  
		add("TC", TXT.get("ISO3166.country.tc.Turks and Caicos Islands", "Turks and Caicos Islands"));  
		add("TV", TXT.get("ISO3166.country.tv.Tuvalu", "Tuvalu"));  
		add("UG", TXT.get("ISO3166.country.ug.Uganda", "Uganda"));  
		add("UA", TXT.get("ISO3166.country.ua.Ukraine", "Ukraine"));  
		add("AE", TXT.get("ISO3166.country.ae.United Arab Emirates", "United Arab Emirates"));  
		add("GB", TXT.get("ISO3166.country.gb.United Kingdom", "United Kingdom"));  
		add("US", TXT.get("ISO3166.country.us.United States", "United States"));  
		add("UM", TXT.get("ISO3166.country.um.United States Minor Outlying Islands", "United States Minor Outlying Islands"));  
		add("UY", TXT.get("ISO3166.country.uy.Uruguay", "Uruguay"));  
		add("UZ", TXT.get("ISO3166.country.uz.Uzbekistan", "Uzbekistan"));  
		add("VU", TXT.get("ISO3166.country.vu.Vanuatu", "Vanuatu"));  
		add("VE", TXT.get("ISO3166.country.ve.Venezuela, Bolivarian Republic of", "Venezuela"));  
		add("VN", TXT.get("ISO3166.country.vn.Viet Nam", "Viet Nam"));  
		add("VG", TXT.get("ISO3166.country.vg.Virgin Islands, British", "Virgin Islands, British"));  
		add("VI", TXT.get("ISO3166.country.vi.Virgin Islands, U.S.", "Virgin Islands, U.S."));  
		add("WF", TXT.get("ISO3166.country.wf.Wallis and Futuna", "Wallis and Futuna"));  
		add("EH", TXT.get("ISO3166.country.eh.Western Sahara", "Western Sahara"));  
		add("YE", TXT.get("ISO3166.country.ye.Yemen", "Yemen"));  
		add("ZM", TXT.get("ISO3166.country.zm.Zambia", "Zambia"));  
		add("ZW", TXT.get("ISO3166.country.zw.Zimbabwe", "Zimbabwe"));
	}
	
	
	private static void add(String code, String country)
	{
		countries.put(code, country);
	}
	
	
	public static String byCountryCode(String code)
	{
		return countries.get(CKit.toUpperCase(code));
	}
}
