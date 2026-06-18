CREATE TABLE users (
    id              VARCHAR(64) PRIMARY KEY,
    role            VARCHAR(32)  NOT NULL,
    auth_provider   VARCHAR(32)  NOT NULL,
    device_id       VARCHAR(128),
    entitlements    JSONB        NOT NULL DEFAULT '[]',
    wechat_mp_open_id VARCHAR(128),
    wechat_oa_open_id VARCHAR(128),
    union_id        VARCHAR(128),
    active_journey_id VARCHAR(64),
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX idx_users_device_id ON users(device_id) WHERE device_id IS NOT NULL;

CREATE TABLE refresh_tokens (
    token       TEXT PRIMARY KEY,
    user_id     VARCHAR(64) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE journeys (
    id                    VARCHAR(64) PRIMARY KEY,
    user_id               VARCHAR(64) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status                VARCHAR(32) NOT NULL,
    current_step          VARCHAR(32) NOT NULL,
    steps                 JSONB       NOT NULL DEFAULT '{}',
    resume_file_id        VARCHAR(64),
    structured_resume_id  VARCHAR(64),
    interview_session_id  VARCHAR(64),
    talent_profile_id     VARCHAR(64),
    created_at            TIMESTAMPTZ NOT NULL,
    updated_at            TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_journeys_user_id ON journeys(user_id);

CREATE TABLE resume_files (
    id            VARCHAR(64) PRIMARY KEY,
    journey_id    VARCHAR(64) NOT NULL REFERENCES journeys(id) ON DELETE CASCADE,
    file_name     VARCHAR(512) NOT NULL,
    file_type     VARCHAR(32)  NOT NULL,
    file_size     BIGINT       NOT NULL,
    storage_path  TEXT         NOT NULL,
    uploaded_at   TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_resume_files_journey_id ON resume_files(journey_id);

CREATE TABLE resume_raw_text (
    id              VARCHAR(64) PRIMARY KEY,
    journey_id      VARCHAR(64) NOT NULL REFERENCES journeys(id) ON DELETE CASCADE,
    resume_file_id  VARCHAR(64) REFERENCES resume_files(id) ON DELETE SET NULL,
    extracted_text  TEXT        NOT NULL,
    extract_method  VARCHAR(32) NOT NULL DEFAULT 'pdfbox',
    extracted_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX idx_resume_raw_text_journey_id ON resume_raw_text(journey_id);

CREATE TABLE structured_resumes (
    id                  VARCHAR(64) PRIMARY KEY,
    journey_id          VARCHAR(64) NOT NULL REFERENCES journeys(id) ON DELETE CASCADE,
    basic_info          JSONB        NOT NULL DEFAULT '{}',
    education           JSONB        NOT NULL DEFAULT '[]',
    work_experience     JSONB        NOT NULL DEFAULT '[]',
    projects            JSONB        NOT NULL DEFAULT '[]',
    skills              JSONB        NOT NULL DEFAULT '[]',
    parse_quality_score DOUBLE PRECISION NOT NULL DEFAULT 0,
    confidence          VARCHAR(16)  NOT NULL DEFAULT 'medium',
    warnings            JSONB        NOT NULL DEFAULT '[]',
    missing_fields      JSONB        NOT NULL DEFAULT '[]',
    updated_at          TIMESTAMPTZ  NOT NULL
);

CREATE UNIQUE INDEX idx_structured_resumes_journey_id ON structured_resumes(journey_id);

CREATE TABLE interview_sessions (
    id                  VARCHAR(64) PRIMARY KEY,
    journey_id          VARCHAR(64) NOT NULL REFERENCES journeys(id) ON DELETE CASCADE,
    stage               VARCHAR(64) NOT NULL,
    status              VARCHAR(32) NOT NULL,
    missing_evidence    JSONB       NOT NULL DEFAULT '[]',
    question_index      INT         NOT NULL DEFAULT 0,
    can_generate_profile BOOLEAN    NOT NULL DEFAULT FALSE
);

CREATE UNIQUE INDEX idx_interview_sessions_journey_id ON interview_sessions(journey_id);

CREATE TABLE interview_turns (
    id                   VARCHAR(64) PRIMARY KEY,
    session_id           VARCHAR(64) NOT NULL REFERENCES interview_sessions(id) ON DELETE CASCADE,
    role                 VARCHAR(16) NOT NULL,
    content              TEXT        NOT NULL,
    question_reason      TEXT,
    target_capabilities  JSONB       NOT NULL DEFAULT '[]',
    created_at           TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_interview_turns_session_id ON interview_turns(session_id);

CREATE TABLE talent_profiles (
    id                VARCHAR(64) PRIMARY KEY,
    journey_id        VARCHAR(64) NOT NULL REFERENCES journeys(id) ON DELETE CASCADE,
    candidate         JSONB        NOT NULL DEFAULT '{}',
    summary           TEXT         NOT NULL,
    overall_score     INT          NOT NULL,
    capabilities      JSONB        NOT NULL DEFAULT '[]',
    strengths         JSONB        NOT NULL DEFAULT '[]',
    risks             JSONB        NOT NULL DEFAULT '[]',
    preferences       JSONB        NOT NULL DEFAULT '[]',
    recommended_roles JSONB        NOT NULL DEFAULT '[]',
    confidence        VARCHAR(16)  NOT NULL DEFAULT 'medium',
    evidence          JSONB        NOT NULL DEFAULT '[]',
    generated_at      TIMESTAMPTZ  NOT NULL
);

CREATE UNIQUE INDEX idx_talent_profiles_journey_id ON talent_profiles(journey_id);

CREATE TABLE resume_versions (
    id                VARCHAR(64) PRIMARY KEY,
    journey_id        VARCHAR(64) NOT NULL REFERENCES journeys(id) ON DELETE CASCADE,
    version_key       VARCHAR(32)  NOT NULL,
    title             TEXT         NOT NULL,
    content           TEXT         NOT NULL,
    content_format    VARCHAR(16)  NOT NULL DEFAULT 'plain',
    confidence        VARCHAR(16)  NOT NULL DEFAULT 'medium',
    warnings          JSONB        NOT NULL DEFAULT '[]',
    used_evidence_ids JSONB        NOT NULL DEFAULT '[]',
    generated_at      TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_resume_versions_journey_id ON resume_versions(journey_id);

CREATE TABLE async_tasks (
    id           VARCHAR(64) PRIMARY KEY,
    journey_id   VARCHAR(64) NOT NULL REFERENCES journeys(id) ON DELETE CASCADE,
    type         VARCHAR(32) NOT NULL,
    status       VARCHAR(32) NOT NULL,
    progress     INT         NOT NULL DEFAULT 0,
    result       JSONB,
    error        JSONB,
    metadata     JSONB       NOT NULL DEFAULT '{}',
    created_at   TIMESTAMPTZ NOT NULL,
    updated_at   TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ
);

CREATE INDEX idx_async_tasks_journey_id ON async_tasks(journey_id);
