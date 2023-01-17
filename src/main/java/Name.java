
public class Name {
	private String common;
	private String official;
	NativeName NativeNameObject;

	// Getter Methods

	public String getCommon() {
		return common;
	}

	public String getOfficial() {
		return official;
	}

	public NativeName getNativeName() {
		return NativeNameObject;
	}

	// Setter Methods

	public void setCommon(String common) {
		this.common = common;
	}

	public void setOfficial(String official) {
		this.official = official;
	}

	public void setNativeName(NativeName nativeNameObject) {
		this.NativeNameObject = nativeNameObject;
	}
}
