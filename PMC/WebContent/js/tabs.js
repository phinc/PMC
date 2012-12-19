//CustomTabs class
function CustomTabs(tabId, onTabShow) {
	this.tabId = tabId;
	if (onTabShow) {
		this.tab = $('#'+tabId).tabs({show: onTabShow});
	} else {
		this.tab = $('#'+tabId).tabs();
	}
}

CustomTabs.prototype={
		reloadTab: function(index, url){
			if (url) {
				this.tab.tabs("url", index, url);
			}
			this.tab.tabs("load", index);			
		},
		reloadSelectedTab: function(id) {
			var selected = this.tab.tabs('option', 'selected');
			var url = tabs.getSelectedUrl();
			var pattern = /[?]id[=]\d+/;
			url = url.replace(pattern, "");
			if (id != null) {
				url = url + "?id=" + id;
			}
			this.tab.tabs("url", selected, url);
			this.tab.tabs("load", selected);
		},
		selectTab: function(index) {
			this.tab.tabs("select", index);
		},
		getSelectedUrl: function(){
			var selected = this.tab.tabs('option', 'selected');
			var links = $("#"+this.tabId+" a");
			return $.data(links[selected], 'href.tabs');
		},
		//tabs should be an array of objects with type Tab
		rebuild: function(tabSet) {
			this.tabSet = tabSet;
			var length = this.tab.tabs("length") - 1;
			//delete existing tabs
			for (length; length >= 0; length--) {
				this.tab.tabs("remove", length);
			}
			//create new tabs
			for (var i = 0; i < tabSet.length; i++) {
				this.tab.tabs("add", tabSet[i].getUrl(), tabSet[i].label);
			}
		},
		getSelectedTab: function() {
			return  this.tab.tabs('option', 'selected');
		}
};

