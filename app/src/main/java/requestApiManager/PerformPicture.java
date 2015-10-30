package requestApiManager;

import interfaces.IRequestProcess;
import sdk.SDKPicture;
import sdk.SDKUser;

public class PerformPicture {
	public class PerformPictureCreate implements IRequestProcess {
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKPicture picture = (SDKPicture)obj[1];
			result = new Object[]{obj[0], picture.create()};
			return result;
		}
	}
	
	public class PerformPictureRead implements IRequestProcess {
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKPicture picture = (SDKPicture)obj[1];
			result = new Object[]{obj[0], picture.getById()};
			return result;
		}
	}
	
	public class PerformPictureReadAll implements IRequestProcess {
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKPicture picture = (SDKPicture)obj[1];
			result = new Object[]{obj[0], picture.getAll()};
			return result;
		}
	}
	
	public class PerformPictureReadByUserId implements IRequestProcess {
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKPicture picture = (SDKPicture)obj[1];
			SDKUser user = (SDKUser)obj[2];
			result = new Object[]{obj[0], picture.getByUserId(user.getId())};
			return result;
		}
	}
	
	public class PerformPictureReadProfilePicture implements IRequestProcess {
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKPicture picture = (SDKPicture)obj[1];
			SDKUser user = (SDKUser)obj[2];
			result = new Object[]{obj[0], picture.getProfilePicture(user.getId()), user};
			return result;
		}
	}
	
	public class PerformPictureDelete implements IRequestProcess {
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKPicture picture = (SDKPicture)obj[1];
			result = new Object[]{obj[0], picture.delete()};
			return result;
		}
	}
	
	public class PerformPictureUpdate implements IRequestProcess {
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKPicture picture = (SDKPicture)obj[1];
			result = new Object[]{obj[0], picture.update()};
			return result;
		}
	}
}
