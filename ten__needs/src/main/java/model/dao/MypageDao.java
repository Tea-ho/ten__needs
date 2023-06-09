package model.dao;

import java.sql.ResultSet;

import model.dto.MypageGameDto;

public class MypageDao extends Dao {
	
	private static MypageDao dao = new MypageDao();
	private MypageDao() {}
	public static MypageDao getInstance() { return dao; }
	
	// --------------------------------------------------- MemberDao랑 합치기 작업 필요
	// 회원 탈퇴
	public boolean onDelete( String mId, String mPw ) {
			System.out.println( mId );		System.out.println( mPw );
		String sql = "delete from member where mId = ? and mPw = ?";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, mId );		ps.setString(2, mPw );
			
			if( ps.executeUpdate() == 1 ) { return true; } // --- 확인 필요
		} catch (Exception e) { System.out.println(e); }
		return false;
	}
	
	// 게임 정보 출력
	public MypageGameDto printGameInfo( int mNo ) {
		
		String sql = "select count(*) as gCount, sum(gsResult) as gWin, ROUND((sum(gsResult)/count(*))*100,2) as gWinRate from gamestatus where mNo ="+mNo;
			// System.out.println("sql1 matters"); //--- 문제 없음
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if( rs.next() ) {
				MypageGameDto dto = new MypageGameDto();
					// System.out.println( "sql1 checking" ); //--- if문 가동 확인
				dto.setgCount(rs.getInt(1));
				dto.setgWin(rs.getInt(2));
				dto.setgWinRate(rs.getDouble(3));
				
					System.out.println("mNo cheking: " + mNo);
				sql = "select r.rNo, r.rName, r.rImg, sum(g.gsResult) as total from gamestatus g join racket r on g.rNo = r.rNo where mNo = ? group by r.rNo order by total desc";
				ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ps.setInt(1, mNo);
				ResultSet rs2 = ps.executeQuery();
				
					System.out.println( rs2 );
				
				while( rs2.next() ) { // --- test해야함
					if( rs2.first() ) {
						dto.setgBestRa(rs2.getString(2));
						dto.setrImg(rs2.getString(3));
					}
					if( rs2.last() ) {
						dto.setgWorstRa(rs2.getString(2));
					}
				}
				return dto;
			}
		} catch (Exception e) { System.out.println(e); }
		return null;
	}
}
