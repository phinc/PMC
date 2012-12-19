package by.phinc.pmc.model.beans;

public class Document implements IModel<Integer>, Comparable<Document> {
	
	private Integer id;
	
	//private DocumentOwner owner;
	
	private String filename;
	
	private String name;
	
	private String description;
	
	private String path;
	
	public Document() {
		super();
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer pId) {
		this.id = pId;
	}

//	public DocumentOwner getOwner() {
//		return owner;
//	}
//
//	public void setOwner(DocumentOwner owner) {
//		this.owner = owner;
//	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public int compareTo(Document arg0) {
		return getFilename().compareTo(arg0.getFilename());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Document)) {
			return false;
		}
		Document doc = (Document)obj;
		return (filename == null) ? doc.getFilename() == null 
									: filename.equals(doc.getFilename());
	}

	@Override
	public int hashCode() {
		return (filename != null) ? filename.hashCode() : super.hashCode();
	}

	@Override
	public String toString() {
		return "id=" + id + "; filename=" + filename + "; name=" + name;
	}
	
	
}
