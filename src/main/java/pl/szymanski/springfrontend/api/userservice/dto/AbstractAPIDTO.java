package pl.szymanski.springfrontend.api.userservice.dto;

import java.util.List;

public abstract class AbstractAPIDTO<T> {

	private List<T> content;

	private PageDTO page;

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public PageDTO getPage() {
		return page;
	}

	public void setPage(PageDTO page) {
		this.page = page;
	}

}
