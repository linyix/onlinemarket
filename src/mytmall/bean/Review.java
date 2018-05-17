package mytmall.bean;

import mytmall.bean.Product;
import mytmall.bean.User;
import java.util.*;
import mytmall.bean.*;
import mytmall.comparator.*;
import mytmall.DAO.*;
import mytmall.util.*;
public class Review {
		private String content;
		private Date createDate;
		private int uid;
		private int ptid;
		private int id;
		private User user;
		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public Date getCreateDate() {
			return createDate;
		}
		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}

		public int getUid() {
			return uid;
		}

		public void setUid(int uid) {
			this.uid = uid;
		}

		public int getPtid() {
			return ptid;
		}

		public void setPtid(int ptid) {
			this.ptid = ptid;
		}
		
}
