
-- 예제 테이블 스키마
insert into table_schema (id, schema_name, user_id, exported_at, created_at, created_by, modified_at, modified_by)
values
    (1, 'test_schema1', 'djkeh', null, now(), 'uno', now(), 'uno')
;

-- 테이블 스키마 id를 직접 입력하였으므로 auto increment 수치를 직접 제어해줘야 함
ALTER TABLE table_schema AUTO_INCREMENT = 2;

-- 예제 테이블 스키마 필드
insert into schema_field (table_schema_id, field_order, field_name, mock_data_type,
                          type_option_json, blank_percent, force_value,
                          created_at, created_by, modified_at, modified_by)
values
    (1, 1, 'id', 'ROW_NUMBER', '{"start": 1, "step": 1}', 0, null, now(), 'uno', now(), 'uno'),
    (1, 3, 'age', 'NUMBER', '{"min": 10, "max": 30, "decimals": 0}', 50, null, now(), 'uno', now(), 'uno'),
    (1, 2, 'name', 'STRING', '{"minLength": 1, "maxLength": 10}', 0, null, now(), 'uno', now(), 'uno'),
    (1, 4, 'active', 'BOOLEAN', null, 0, 'true', now(), 'uno', now(), 'uno')
;

-- 샘플 가짜 데이터
insert into mock_data (mock_data_type, mock_data_value)
values
    ('NAME', '김민준'),
    ('NAME', '이서윤'),
    ('NAME', '박지후'),
    ('NAME', '정지우'),
    ('NAME', '최예은'),
    ('NAME', '강지민'),
    ('NAME', '조현우'),
    ('NAME', '윤서현'),
    ('NAME', '임유진'),
    ('NAME', '오하늘'),
    ('NAME', '서지호'),
    ('NAME', '한서준'),
    ('NAME', '배수아'),
    ('NAME', '홍태희'),
    ('NAME', '류지훈'),
    ('NAME', '권유나'),
    ('NAME', '송민서'),
    ('NAME', '김서진'),
    ('NAME', '문지훈'),
    ('NAME', '이가은'),
    ('NAME', '박지민'),
    ('NAME', '전하윤'),
    ('NAME', '정지현'),
    ('NAME', '최지훈'),
    ('NAME', '강지윤'),
    ('NAME', '조예진'),
    ('NAME', '윤지후'),
    ('NAME', '임하늘'),
    ('NAME', '오유진'),
    ('NAME', '서지우'),
    ('NAME', '한서현'),
    ('NAME', '배현우'),
    ('NAME', '홍예은'),
    ('NAME', '류지민'),
    ('NAME', '권서윤'),
    ('NAME', '송지후'),
    ('NAME', '김태윤'),
    ('NAME', '문서윤'),
    ('NAME', '이현우'),
    ('NAME', '박지훈'),
    ('NAME', '전예은'),
    ('NAME', '정하윤'),
    ('NAME', '최유진'),
    ('NAME', '강현우'),
    ('NAME', '조지훈'),
    ('NAME', '윤태희'),
    ('NAME', '임지호'),
    ('NAME', '오지후'),
    ('NAME', '서유나'),
    ('NAME', '한지민'),
    ('NAME', '배서준'),
    ('NAME', '홍지우'),
    ('NAME', '류태희'),
    ('NAME', '권지우'),
    ('NAME', '송지윤'),
    ('NAME', '김하늘'),
    ('NAME', '문현우'),
    ('NAME', '이태희'),
    ('NAME', '박하윤'),
    ('NAME', '전서윤'),
    ('NAME', '정태희'),
    ('NAME', '최지우'),
    ('NAME', '강예은'),
    ('NAME', '조지민'),
    ('NAME', '윤지윤'),
    ('NAME', '임지우'),
    ('NAME', '오지훈'),
    ('NAME', '서태희'),
    ('NAME', '한유진'),
    ('NAME', '배지훈'),
    ('NAME', '홍서윤'),
    ('NAME', '류하늘'),
    ('NAME', '권지민'),
    ('NAME', '송서윤'),
    ('NAME', '김지우'),
    ('NAME', '문태희'),
    ('NAME', '이지후'),
    ('NAME', '박서준'),
    ('NAME', '전하늘'),
    ('NAME', '정서윤'),
    ('NAME', '최현우'),
    ('NAME', '강지우'),
    ('NAME', '조하늘'),
    ('NAME', '윤서진'),
    ('NAME', '임예은'),
    ('NAME', '오지민'),
    ('NAME', '서하늘'),
    ('NAME', '한태희'),
    ('NAME', '배지민'),
    ('NAME', '홍유진'),
    ('NAME', '류서준'),
    ('NAME', '권지후'),
    ('NAME', '송태희'),
    ('NAME', '김지훈'),
    ('NAME', '문하늘'),
    ('NAME', '이지윤'),
    ('NAME', '박태희'),
    ('NAME', '전지민'),
    ('NAME', '정서진'),
    ('NAME', '최하늘')
;
