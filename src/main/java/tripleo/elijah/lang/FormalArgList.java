package tripleo.elijah.lang;

import java.util.*;

// Referenced classes of package pak2:
//			FormalArgListItem

public class FormalArgList {

	private List<FormalArgListItem> falis=new ArrayList<FormalArgListItem>();

	public FormalArgListItem next() {
		FormalArgListItem fali = new FormalArgListItem();
		falis.add(fali);
		return fali;
	}

}
