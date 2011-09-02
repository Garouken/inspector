package cl.votainteligente.inspector.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class ShareThis extends Composite {
	private static ShareThisUiBinder uiBinder = GWT.create(ShareThisUiBinder.class);
	interface ShareThisUiBinder extends UiBinder<Widget, ShareThis> {}

	public enum Align {
		VERTICAL("vertical", "normal-count", "tall", "box_count"),
		HORIZONTAL("horizontal", "small-count", "medium", "button_count");

		private String twitterPropertyName;
		private String googleBuzzPropertyName;
		private String googlePlusPropertyName;
		private String facebookPropertyName;

		private Align(String twitterPropertyName, String googleBuzzPropertyName, String googlePlusPropertyName, String facebookPropertyName) {
			this.twitterPropertyName = twitterPropertyName;
			this.googleBuzzPropertyName = googleBuzzPropertyName;
			this.googlePlusPropertyName = googlePlusPropertyName;
			this.facebookPropertyName = facebookPropertyName;
		}

		public String getTwitterPropertyName() {
			return twitterPropertyName;
		}

		public String getGoogleBuzzPropertyName() {
			return googleBuzzPropertyName;
		}

		public String getGooglePlusPropertyName() {
			return googlePlusPropertyName;
		}

		public String getFacebookPropertyName() {
			return facebookPropertyName;
		}
	}

	@UiField HTMLPanel shareThisPanel;

	private HTML htmlTwitter;
	private HTML htmlGoogleBuzz;
	private HTML htmlGooglePlus;
	private HTML htmlFacebook;
	private String href = "";
	private String title = "";
	private String message = "";
	private String lang = "";
	private String via = "";
	private Align align = Align.VERTICAL;
	private Panel shareThisContainer;

	public ShareThis() {
		initWidget(uiBinder.createAndBindUi(this));
		clear();
	}

	public HTML getHtmlTwitter() {
		return htmlTwitter;
	}

	public void setHtmlTwitter(HTML twitter) {
		this.htmlTwitter = twitter;
	}

	public HTML getHtmlGoogleBuzz() {
		return htmlGoogleBuzz;
	}

	public void setHtmlGoogleBuzz(HTML googleBuzz) {
		this.htmlGoogleBuzz = googleBuzz;
	}

	public HTML getHtmlGooglePlus() {
		return htmlGooglePlus;
	}

	public void setHtmlGooglePlus(HTML htmlGooglePlus) {
		this.htmlGooglePlus = htmlGooglePlus;
	}

	public HTML getHtmlFacebook() {
		return htmlFacebook;
	}

	public void setHtmlFacebook(HTML facebook) {
		this.htmlFacebook = facebook;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public Align getAlign() {
		return align;
	}

	public void setAlign(Align align) {
		this.align = align;
	}

	public void setup() {
		setupTwitterScript();
		setupGoogleBuzzScript();
		setupGooglePlusScript();
		setupFacebookScript();

		setLang(getLocaleLang(true));

		if (align == Align.VERTICAL) {
			shareThisContainer = new VerticalPanel();
		} else {
			shareThisContainer = new HorizontalPanel();
		}

		drawTwitterButton();
		drawGoogleBuzzButton();
		drawGooglePlusButton();
		drawFacebookButton();

		shareThisPanel.add(shareThisContainer);
	}

	public void clear() {
		shareThisPanel.clear();
	}

	private void drawTwitterButton() {
		String html = "<a ";
		html += "href=\"http://twitter.com/share\" ";
		html += "class=\"twitter-share-button\" ";
		html += "data-count=\"" + align.getTwitterPropertyName() + "\" ";
		html += "data-text=\"" + message + "\" ";
		html += "data-url=\"" + href + "\" ";
		html += "data-lang=\"" + lang + "\" ";

		if (via != null && via.length() > 0) {
			html += "data-via=\"" + via + "\" ";
		}

		html += ">Tweet</a>";
		setHtmlTwitter(new HTML(html));
		shareThisContainer.add(htmlTwitter);
	}

	private void drawGoogleBuzzButton() {
		String html = "<a ";
		html += "href=\"http://www.google.com/buzz/post\" ";
		html += "class=\"google-buzz-button\" ";
		html += "title=\"" + title +  "\" ";
		html += "data-message=\"" + message + "\" ";
		html += "data-url=\"" + href +"\" ";
		html += "data-locale=\"en\" ";
		html += "data-button-style=\"" + align.getGoogleBuzzPropertyName() + "\"></a>";

		setHtmlGoogleBuzz(new HTML(html));
		shareThisContainer.add(htmlGoogleBuzz);
	}

	private void drawGooglePlusButton() {
		String html = "<g:plusone ";
		html += "href=\"" + href + "\" ";
		html += "size=\"" + align.getGooglePlusPropertyName() + "\">";
		html += "</g:plusone>";

		setHtmlGooglePlus(new HTML(html));
		shareThisContainer.add(htmlGooglePlus);
	}

	private void drawFacebookButton() {
		String html = "<fb:like ";
		html += "href=\"" + href + "\" ";
		html += "layout=\"" + align.getFacebookPropertyName() + "\" ";
		html += "show_faces=\"false\" ";
		html += "send=\"true\" ";
		html += "width=\"200\">";
		html += "</fb:like>";

		setHtmlFacebook(new HTML(html));
		shareThisContainer.add(htmlFacebook);
	}

	private void setupTwitterScript() {
		Document doc = Document.get();
		ScriptElement script = doc.createScriptElement();
		script.setSrc("http://platform.twitter.com/widgets.js");
		script.setType("text/javascript");
		script.setLang("javascript");
		doc.getBody().appendChild(script);
	}

	private void setupGoogleBuzzScript() {
		Document doc = Document.get();
		ScriptElement script = doc.createScriptElement();
		script.setSrc("http://www.google.com/buzz/api/button.js");
		script.setType("text/javascript");
		script.setLang("javascript");
		doc.getBody().appendChild(script);
	}

	private void setupGooglePlusScript() {
		Document doc = Document.get();
		ScriptElement script = doc.createScriptElement();
		script.setSrc("https://apis.google.com/js/plusone.js");
		script.setType("text/javascript");
		script.setLang("javascript");
		doc.getBody().appendChild(script);
	}

	private void setupFacebookScript() {
		Document doc = Document.get();
		ScriptElement script = doc.createScriptElement();
		script.setSrc("http://connect.facebook.net/" + getLocaleLang(false).trim() + "/all.js#xfbml=1");
		script.setType("text/javascript");
		script.setLang("javascript");
		doc.getBody().appendChild(script);
	}

	private String getLocaleLang(Boolean shortName) {
		LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();
		String localeName = null;

		if (shortName == true) {
			localeName = localeInfo.getLocaleName().substring(0, localeInfo.getLocaleName().indexOf("_"));
		} else {
			localeName = localeInfo.getLocaleName();
		}

		return localeName;
	}
}
