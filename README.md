# MiniGameApp-ColorSelect

틀린 색상 찾기 App 입니다.

게임 종료 후 게임 기록에 대한 데이터를 암호화

## 목표
1. 웹 서버와 데이터베이스의 연동을 통한 미니게임 APP 개발
2. 개발 및 실행 과정에서 발생하는 보안 취약점 점검과 보안 기능 구현


## 설계 환경
![DevEnv](https://github.com/doorisopen/MiniGameApp-ColorSelect/blob/master/img/MiniGameApp_devEnv.JPG)

## 설계 내용 및 결과
### 설계 내용
![devContent](https://github.com/doorisopen/MiniGameApp-ColorSelect/blob/master/img/MiniGameApp_devContent.JPG)

### 설계 결과 상세내용
![devResult1](https://github.com/doorisopen/MiniGameApp-ColorSelect/blob/master/img/MiniGameApp_devResult1.JPG)

### 결론
![devResult2](https://github.com/doorisopen/MiniGameApp-ColorSelect/blob/master/img/MiniGameApp_devResult2.JPG)

[개발 환경]
1. Android Studio
2. Apache 2.4
3. php7
4. MySQL5.7




# *Apache 2.4 설치방법*
아파치 설치 -> Microsoft Visual C++ 2015 Redistributable  설치를 선행한다.
1. 아파치 2.4 설치 httpd -2.4.23-win64-VC14-zip 파일을 받는다
2. 아파치 집 파일의 아파치24를 c 드라이브에 저장한다 . 
3. 아파치 설치폴더 \conf 경로에 httpd.conf  파일을 수정한다. 메모장, 노트패드 상관없음
4. httpd.conf 폴더속 ServerRoot 경로 확인 "c:\Apache24" 여야한다.
5. Listen 80 확인
6. 웹문서 저장 위치 변경 DocumentRoot "c:\Apache24/htdocs" 아래의 Directory 위치와 동일해야함
7. ServerName 이 #으로 주석 처리 되어있다 주석을 지우고 localhost:80 으로 변경한다.
8. 환경변수 설정해준다 c:\Apache24\bin <<으로 
9. cmd를 관리자 권한으로 실행
10. httpd -k install 해서 아파치 서비스 설치한다.
11. httpd -k start 해서 서비스 시작
12. 끝

# *Apache 명령어*
1. httpd -k start //아파치 서비스 시작
2. httpd -k stop // 아파치 서비스 종료(중지)
3. httpd.exe -k restart // 아파치 서비스 재시작
4. httpd.exe -k uninstall //아파치 서비스 제거

# *php7 설치 방법*
php 설치 및 아파치 연동 PHP7.x VC14 버전 설치함 아파치 버전과 동일해야함 VC14
1. c:\php7 로 저장한다.
2. php 폴더 안에 php.ini-production 파일을 복사해서 복사한 파일의 이름을 php.ini 이름으로 변경
3. php.ini 는 환경설정 파일로서의 역할을함 php.ini 를 열어서 수정한다.
4. ;extension_dir = "./" 를 찾아서 ; - 세미콜론 제거 하고 경로를 php설치 "디렉토리/ext" 의 형태로 수정 경로는 / 로 변경
extension_dir = "C:/php7/ext" << 저장하고 빠져나온다
5. 아파치 폴더 \conf 에 httpd.conf 파일을 에디터로 연다 
6. ctrl+F <IfModule dir_module> 찾아서 DirectoryIndex에 index.php을 index.html 앞에 넣는다. 
7. 그리고 폴더 맨아래에 다음 구문을 추가한다.

PHPIniDir "C:/php7"
1. LoadModule php7_module "C:/php7/php7apache2_4.dll"
2. AddType application/x-httpd-php .html .php
3. AddHandler application/x-httpd-php .php

8.아파치 서비스 재시작 한다 . httpd -k restart 입력 
9. phpinfo.php -> <?php phpinfo()?> 작성후 주소창에 localhost/phpinfo.php 
안된다면 재부팅 한다 . 아니면 재배포 가능 패키지를 32비트로 교체해서 진행해본다.

# *MySQL5.7 설치방법*
1. https://dev.mysql.com/downloads/mysql/ 에 접속하여 5.7버전 다운로드
2. MySQL설치 경로(MySQL폴더 내 bin 폴더까지)를 환경변수 등록작업한다. -> 명령프롬프트(관리자)에서 MySQL 명령어를 입력하여 실행 가능함
3. MySQL폴더 내에 옵션 파일 생성 (my.ini) 파일 생성시 인코딩 타입 ANSI or ASCII로 설정할 것.
[주의!!]
만약 UTF-8로 설정 될 경우....
mysqld:[ERROR] Found oprion without proceding group in config file C:\mysql\my.ini at line 1! 와 같은 메시지를 볼 수 있음  

4. cmd (관리자)를 열고 mysqld.exe --initialize 입력 -> MySQL 설치 폴더 내에 data 폴더 생성된 것을 확인
(data폴더는 DB데이터가 저장되는 경로이다.)
5. data폴더 내에 DESKTOP-***.err 에러로그 파일이 있다. 메모장으로 열어보면 root@localhost:(password) 패스워드 확인
6. mysql -u root -p 입력 하고 MySQL설치시 입력한 비밀번호 입력 후 MySql 접속
7. mysql 패스워드 변경 방법 set password = 'password 입력'; 하여 변경 가능

-DataBase 서비스 시작하기 명령어 -> net start mysql



->my.ini 파일 내용 (하단의 내용을 입력하여 저장할것. 폴더 경로는 백슬래시(\)가 아닌 슬래시(/)로 써줘야함)
1. [mysqld]
2. #set basedir to your intallation path
3. basedir=C:/mysql
4. #set datadir to the location of your data directory
5. datadir=C:/mysql/data
6. port=3306
