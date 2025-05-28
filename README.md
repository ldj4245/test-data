# 📊 테스트 데이터 생성기 (Test Data Generator)

<div align="center">
  <img src="https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=java&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.3.3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot 3.3.3" />
  <img src="https://img.shields.io/badge/Gradle-8.8-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle 8.8" />
  <img src="https://img.shields.io/badge/Heroku-Deployed-430098?style=for-the-badge&logo=heroku&logoColor=white" alt="Heroku" />
</div>

<div align="center">
  <a href="https://test-data-production-218bfd529fad.herokuapp.com/" target="_blank">
    <img src="https://img.shields.io/badge/Live%20Demo-Visit%20Site-FF5700?style=for-the-badge&logo=safari&logoColor=white" alt="Live Demo" />
  </a>
</div>

<br>

<p align="center">
  <b>다양한 형식의 테스트 데이터를 손쉽게 생성하고 내보낼 수 있는 웹 애플리케이션</b>
</p>

## 📝 프로젝트 소개

**테스트 데이터 생성기**는 개발자와 QA 엔지니어를 위한 강력한 테스트 데이터 생성 도구입니다. 사용자 정의 스키마를 기반으로 다양한 형식(CSV, JSON, SQL, XML, Excel)의 실제같은 테스트 데이터를 자동으로 생성합니다. GitHub 계정으로 로그인하여 스키마를 저장하고 관리할 수 있으며, 다크 모드를 지원하는 모던한 UI를 제공합니다.


## ✨ 주요 기능

### 스키마 정의 및 관리
- **직관적인 스키마 빌더**: 필드 타입, 이름, 제약조건을 쉽게 설정
- **드래그 앤 드롭**: 필드 순서를 드래그로 변경 가능
- **저장 및 불러오기**: 로그인한 사용자는 스키마 저장 및 관리 가능
- **템플릿 제공**: 회원 정보, 상품 정보, 주문 정보, 주소 등 미리 정의된 템플릿

### 데이터 생성 및 내보내기
- **다중 형식 지원**: CSV, TSV, JSON, XML, SQL INSERT, Excel
- **데이터 미리보기**: 내보내기 전 데이터 샘플 확인
- **필드별 빈 값 비율 설정**: 현실적인 데이터 생성을 위한 세부 설정

### 사용자 경험
- **GitHub OAuth 로그인**: 간편한 인증
- **다크 모드 지원**: 사용자 환경에 맞는 테마 선택
- **반응형 디자인**: 모바일, 태블릿, 데스크톱 지원

## 🛠️ 기술 스택

<table>
  <tr>
    <td>
      <strong>백엔드</strong>
    </td>
    <td>
      <img src="https://img.shields.io/badge/Java-21-007396?style=flat&logo=java&logoColor=white" alt="Java 21" /> 
      <img src="https://img.shields.io/badge/Spring%20Boot-3.3.3-6DB33F?style=flat&logo=spring-boot&logoColor=white" alt="Spring Boot 3.3.3" /> 
      <img src="https://img.shields.io/badge/JPA-Hibernate-59666C?style=flat&logo=hibernate&logoColor=white" alt="JPA" /> 
      <img src="https://img.shields.io/badge/Spring%20Security-OAuth2-6DB33F?style=flat&logo=spring-security&logoColor=white" alt="Spring Security" />
    </td>
  </tr>
  <tr>
    <td>
      <strong>프론트엔드</strong>
    </td>
    <td>
      <img src="https://img.shields.io/badge/Thymeleaf-3-005F0F?style=flat&logo=thymeleaf&logoColor=white" alt="Thymeleaf" /> 
      <img src="https://img.shields.io/badge/Tailwind%20CSS-3-38B2AC?style=flat&logo=tailwind-css&logoColor=white" alt="Tailwind CSS" /> 
      <img src="https://img.shields.io/badge/JavaScript-ES6-F7DF1E?style=flat&logo=javascript&logoColor=black" alt="JavaScript" />
    </td>
  </tr>
  <tr>
    <td>
      <strong>데이터베이스</strong>
    </td>
    <td>
      <img src="https://img.shields.io/badge/H2%20Database-Development-02758F?style=flat&logo=h2&logoColor=white" alt="H2 Database" /> 
      <img src="https://img.shields.io/badge/MySQL-Production-4479A1?style=flat&logo=mysql&logoColor=white" alt="MySQL" />
    </td>
  </tr>
  <tr>
    <td>
      <strong>기타 라이브러리</strong>
    </td>
    <td>
      <img src="https://img.shields.io/badge/Jackson-2.17.2-000000?style=flat" alt="Jackson" /> 
      <img src="https://img.shields.io/badge/Apache%20POI-Excel%20Export-C71A36?style=flat" alt="Apache POI" /> 
      <img src="https://img.shields.io/badge/JUnit-5.10.3-25A162?style=flat&logo=junit5&logoColor=white" alt="JUnit 5" /> 
      <img src="https://img.shields.io/badge/Mockito-5.11-6DB33F?style=flat" alt="Mockito" />
    </td>
  </tr>
  <tr>
    <td>
      <strong>도구 및 배포</strong>
    </td>
    <td>
      <img src="https://img.shields.io/badge/Gradle-8.8-02303A?style=flat&logo=gradle&logoColor=white" alt="Gradle" /> 
      <img src="https://img.shields.io/badge/Heroku-Deployment-430098?style=flat&logo=heroku&logoColor=white" alt="Heroku" /> 
      <img src="https://img.shields.io/badge/Git-Version%20Control-F05032?style=flat&logo=git&logoColor=white" alt="Git" />
    </td>
  </tr>
</table>

## 🔍 API 구조

### 웹 엔드포인트
| 경�� | 메서드 | 설명 | 인증 필요 |
|------|--------|------|-----------|
| `/` | GET | 메인 페이지 | ❌ |
| `/table-schema` | GET | 테이블 스키마 생성 페이지 | ❌ |
| `/table-schema` | POST | 테이블 스키마 저장 | ✅ |
| `/table-schema/my-schemas` | GET | 내 스키마 목록 | ✅ |
| `/table-schema/my-schemas/{schemaName}` | POST | 스키마 삭제 | ✅ |
| `/my-account` | GET | 내 계정 정보 | ✅ |

### REST API 엔드포인트
| 경로 | 메서드 | 설명       | 응답 형식 |
|------|--------|----------|-----------|
| `/table-schema/export` | GET | 데이터 내보내기 | CSV, TSV, JSON, XML, SQL, Excel |
| `/table-schema/preview` | GET | 데이터 미리보기 | JSON |

## 🏗️ 아키텍처

프로젝트는 클래식한 Spring MVC 아키텍처를 따르며, 디자인 패턴을 적극 활용하여 확장성 있는 구조로 설계되었습니다.

### 계층 구조
```
┌─────────────────┐         ┌─────────────────┐         ┌─────────────────┐
│    Controller   │ ------► │     Service     │ ------► │   Repository    │
└─────────────────┘         └─────────────────┘         └─────────────────┘
        │                           │                           │
        │                           │                           │
        ▼                           ▼                           ▼
┌─────────────────┐         ┌─────────────────┐         ┌─────────────────┐
│      View       │         │     Domain      │         │    Database     │
│   (Thymeleaf)   │         │     Model       │         │   (H2/MySQL)    │
└─────────────────┘         └─────────────────┘         └─────────────────┘
```

### 디자인 패턴
- **전략 패턴**: 다양한 파일 형식별 내보내기 전략 구현 (`MockDataFileExporter` 인터페이스)
- **컨텍스트 패턴**: 데이터 생성 및 내보내기 컨텍스트 관리 (`MockDataGeneratorContext`, `MockDataFileExporterContext`)
- **레포지토리 패턴**: 데이터 접근 추상화 (`TableSchemaRepository`)
- **DTO 패턴**: 계층간 데이터 전송 객체 활용

### 주요 컴포넌트 다이어그램
```
┌─────────────────────────────────────────────────────────────────┐
│                        TableSchemaController                    │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                                ▼
┌───────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│ TableSchemaService │◄───┤SchemaExportSvc  │───►│ FileExporterCtx │
└───────────┬────────┘    └─────────────────┘    └────────┬────────┘
            │                                             │
            ▼                                             ▼
┌───────────────────┐                          ┌─────────────────────┐
│ SchemaRepository  │                          │ ConcreteExporters   │
└───────────────────┘                          │ ┌───────┐ ┌───────┐ │
                                               │ │ CSV   │ │ JSON  │ │
                                               │ └───────┘ └───────┘ │
                                               │ ┌───────┐ ┌───────┐ │
                                               │ │ XML   │ │ Excel │ │
                                               │ └───────┘ └───────┘ │
                                               └─────────────────────┘
```

## 💾 데이터 모델

### 핵심 엔티티

#### TableSchema
```java
@Entity
public class TableSchema extends AuditingFields {
    @Id @GeneratedValue
    private Long id;
    @Column(nullable = false) private String schemaName;
    @Column(nullable = false) private String userId;
    private LocalDateTime exportedAt;
    
    @OneToMany(mappedBy = "tableSchema", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<SchemaField> schemaFields = new LinkedHashSet<>();
}
```

#### SchemaField
```java
@Entity
public class SchemaField extends AuditingFields {
    @Id @GeneratedValue
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private TableSchema tableSchema;
    
    @Column(nullable = false) private String fieldName;
    @Enumerated(EnumType.STRING) private MockDataType mockDataType;
    private Integer fieldOrder;
    private Integer blankPercent;
    private String typeOptionJson;
    private String forceValue;
}
```


## 📁 프로젝트 구조

```
src/
├── main/
│   ├── java/org/leedae/testdata/
│   │   ├── config/                # Spring 구성 클래스
│   │   │   └── SecurityConfig.java
│   │   ├── controller/            # MVC 컨트롤러
│   │   │   ├── MainController.java
│   │   │   └── TableSchemaController.java
│   │   ├── domain/                # 도메인 모델
│   │   │   ├── AuditingFields.java
│   │   │   ├── SchemaField.java
│   │   │   ├── TableSchema.java
│   │   │   └── constant/          # 상수 및 열거형
│   │   │       ├── ExportFileType.java
│   │   │       └── MockDataType.java
│   │   ├── dto/                   # 데이터 전송 객체
│   │   │   ├── request/
│   │   │   ├── response/
│   │   │   └── security/
│   │   ├── repository/            # 데이터 액세스 계층
│   │   │   └── TableSchemaRepository.java
│   │   └── service/               # 비즈니스 로직
│   │       ├── SchemaExportService.java
│   │       ├── TableSchemaService.java
│   │       ├── exporter/          # 파일 내보내기 구현
│   │       │   ├── CSVFileExporter.java
│   │       │   ├── JSONFileExporter.java
│   │       │   ├── MockDataFileExporter.java
│   │       │   ├── MockDataFileExporterContext.java
│   │       │   ├── SQLInsertFileExporter.java
│   │       │   ├── TSVFileExporter.java
│   │       │   ├── XMLFileExporter.java
│   │       │   └── ExcelFileExporter.java
│   │       └── generator/         # 데이터 생성 구현
│   │           ├── MockDataGenerator.java
│   │           ├── MockDataGeneratorContext.java
│   │           └── concretes/     # 구체적인 생성기
│   └── resources/
│       ├── static/                # 정적 자원
│       │   └── js/
│       │       └── darkmode.js    # 다크모드 기능
│       ├── templates/             # Thymeleaf 템플릿
│       │   ├── index.html
│       │   ├── my-account.html
│       │   ├── my-schemas.html
│       │   └── table-schema.html
│       ├── application.yaml       # 애플리케이션 설정
│       └── data.sql               # 초기 데이터
└── test/                          # 테스트 코드
    └── java/org/leedae/testdata/
        ├── service/
        │   ├── SchemaExportServiceTest.java
        │   ├── TableSchemaServiceTest.java
        │   ├── exporter/
        │   │   ├── CSVFileExporterTest.java
        │   │   ├── JSONFileExporterTest.java
        │   │   ├── MockDataFileExporterContextTest.java
        │   │   ├── SQLInsertFileExporterTest.java
        │   │   ├── TSVFileExporterTest.java
        │   │   ├── XMLFileExporterTest.java
        │   │   └── ExcelFileExporterTest.java
        │   └── generator/
        │       ├── MockDataGeneratorContextTest.java
        │       └── StringGeneratorTest.java
        └── controller/
```


## 📈 향후 계획

- [ ] 관계형 스키마 지원 (테이블 간 관계 설정)
- [ ] 더 많은 데이터 타입 추가 (지리 데이터, 복합 타입 등)
- [ ] 스키마 공유 및 팀 협업 기능
- [ ] 대량 데이터 처리 최적화 (100만 행 이상)
- [ ] 더 많은 내보내기 형식 지원 (YAML, Protocol Buffers 등)

## 👨‍💻 작성자



- GitHub: [@YourGitHub](https://github.com/ldj4245)

