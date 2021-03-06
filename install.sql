CREATE TABLE cycles(
	id BIGINT UNSIGNED AUTO_INCREMENT,
	name VARCHAR(20) NOT NULL,
	created_at DATETIME NOT NULL,
	updated_at DATETIME NOT NULL,
	closed_at DATETIME,
	PRIMARY KEY (id)
) ENGINE=InnoDB
COMMENT="Ciclos escolares";

CREATE TABLE classrooms (
	id BIGINT UNSIGNED AUTO_INCREMENT,
	name VARCHAR(20) NOT NULL,
	description VARCHAR(50),
	created_at DATETIME NOT NULL,
	updated_at DATETIME NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB
COMMENT="Catalogo de salones de clases";

CREATE TABLE users (
	id BIGINT UNSIGNED AUTO_INCREMENT,
	name VARCHAR(40) NOT NULL,
	last_name VARCHAR(40) NOT NULL,
	birthday DATE NOT NULL,
	username VARCHAR(20) NOT NULL,
	`password` VARCHAR(255) NOT NULL,
	email VARCHAR(255),
	active BOOLEAN NOT NULL DEFAULT TRUE,
	login_attempt INT NOT NULL DEFAULT 0,
	locked BOOLEAN NOT NULL DEFAULT FALSE,
	created_at DATETIME NOT NULL,
	updated_at DATETIME NOT NULL,
	deleted_at DATETIME,
	PRIMARY KEY (id),
	CONSTRAINT uq_users_username UNIQUE (username),
	CONSTRAINT uq_users_email UNIQUE (email)
) ENGINE=InnoDB
COMMENT="Usuarios del sistema";

CREATE TABLE roles (
	id BIGINT UNSIGNED AUTO_INCREMENT,
	name VARCHAR(40) NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT uq_roles_rol UNIQUE (name)
) ENGINE=InnoDB
COMMENT="Catalogo de roles";

CREATE TABLE user_roles (
	id BIGINT UNSIGNED AUTO_INCREMENT,
	user_id BIGINT UNSIGNED NOT NULL,
	role_id BIGINT UNSIGNED NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users(id),
	CONSTRAINT fk_user_roels_role_id FOREIGN KEY (role_id) REFERENCES roles(id)
) ENGINE=InnoDB
COMMENT="Roles asociados al usuario";

CREATE TABLE courses(
	id BIGINT UNSIGNED AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	created_at DATETIME NOT NULL,
	updated_at DATETIME NOT NULL,
	manager_user_id BIGINT UNSIGNED,
	cycle_id BIGINT UNSIGNED NOT NULL,
	classroom_id BIGINT UNSIGNED,
	primary key (id),
	CONSTRAINT fk_courses_cycle_id FOREIGN KEY (cycle_id) REFERENCES cycles(id),
	CONSTRAINT fk_courses_classroom_id FOREIGN KEY (classroom_id) REFERENCES classrooms(id),
	CONSTRAINT fk_courses_manager_user_id FOREIGN KEY (manager_user_id) REFERENCES users(id)
) ENGINE=InnoDB
COMMENT="Cursos de cada ciclo escolar";

CREATE TABLE course_students(
	id BIGINT UNSIGNED AUTO_INCREMENT,
	course_id BIGINT UNSIGNED NOT NULL,
	student_id BIGINT UNSIGNED NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT fk_course_id FOREIGN KEY (course_id) REFERENCES courses (id),
	CONSTRAINT fk_student_id FOREIGN KEY (student_id) REFERENCES users (id),
	CONSTRAINT uq_course_student UNIQUE(course_id, student_id)
) ENGINE=InnoDB
COMMENT="Estudiantes suscritos a cursos";

CREATE TABLE subjects (
	id BIGINT UNSIGNED AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	course_id BIGINT UNSIGNED NOT NULL,
	classroom_id BIGINT UNSIGNED NOT NULL,
	teacher_id BIGINT UNSIGNED NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT fk_subjects_course_id FOREIGN KEY (course_id) REFERENCES courses(id),
	CONSTRAINT fk_subjects_classroom_id FOREIGN KEY (classroom_id) REFERENCES classrooms(id),
	CONSTRAINT fk_subjects_teacher_id FOREIGN KEY (teacher_id) REFERENCES users(id)
) ENGINE=InnoDB
COMMENT="Materias registradas a cursos";

CREATE TABLE subject_schedules (
	id BIGINT UNSIGNED AUTO_INCREMENT,
	subject_id BIGINT UNSIGNED NOT NULL,
	week_day INT UNSIGNED NOT NULL,
	start_time TIME NOT NULL,
	end_time TIME NOT NULL,
	PRIMARY KEY(id),
	CONSTRAINT fk_subject_schedules_subject_id FOREIGN KEY (subject_id) REFERENCES subjects(id)
) ENGINE=InnoDB
COMMENT="Horarios de clases";

CREATE TABLE grades (
	id BIGINT UNSIGNED AUTO_INCREMENT,
	subject_id BIGINT UNSIGNED NOT NULL,
	description VARCHAR(50) NOT NULL,
	created_at DATETIME NOT NULL,
	updated_at DATETIME NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT fk_grades_subject_id FOREIGN KEY (subject_id) REFERENCES subjects(id)
) ENGINE=InnoDB
COMMENT="Evaluaciones o registro de cuadros de notas por materia";

CREATE TABLE grade_details(
	id BIGINT UNSIGNED AUTO_INCREMENT,
	grade_id BIGINT UNSIGNED NOT NULL,
	student_id BIGINT UNSIGNED NOT NULL,
	score DECIMAL(4, 2) NOT NULL,
	created_at DATETIME NOT NULL,
	updated_at DATETIME NOT NULL,
	deleted_at DATETIME,
	delete_comment VARCHAR(50),
	PRIMARY KEY (id),
	CONSTRAINT fk_grade_details_grade_id FOREIGN KEY (grade_id) REFERENCES grades(id),
	CONSTRAINT fk_grades_student_id FOREIGN KEY (student_id) REFERENCES users(id)
) ENGINE=InnoDB
COMMENT="Detaille de notas";

insert into roles (name) values ("ROLE_ADMIN");
insert into roles (name) values ("ROLE_STUDENT");
insert into roles (name) values ("ROLE_TEACHER");
insert into roles (name) values ("ROLE_STUDENT_DATA_EDITOR");

insert into users 
(name, last_name, birthday, username, `password`, email, active, created_at, updated_at) 
values 
("School", "Manager", now(), "manager", "$2a$10$5dYDQRJaGLMIiXv49goz0uGdZCmrZQ1shakWGy0446SE09dSwqbA2",
"admin@sms.com", true, now(), now());

insert into user_roles (user_id, role_id) values (1, 1);