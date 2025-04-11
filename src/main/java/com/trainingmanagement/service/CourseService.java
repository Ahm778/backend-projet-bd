package com.trainingmanagement.service;

import com.trainingmanagement.dto.CourseDto;
import com.trainingmanagement.dto.UserDto;
import com.trainingmanagement.model.Course;
import com.trainingmanagement.model.User;
import com.trainingmanagement.repository.CourseRepository;
import com.trainingmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return mapToDto(course);
    }

    public List<CourseDto> getCoursesByInstructor(Long instructorId) {
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        return courseRepository.findByInstructor(instructor).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> getCoursesByStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return courseRepository.findByStudentsContaining(student).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CourseDto createCourse(CourseDto courseDto) {
        Course course = new Course();
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setCoverImage(courseDto.getCoverImage());
        course.setStartDate(courseDto.getStartDate());
        course.setEndDate(courseDto.getEndDate());
        course.setCategory(courseDto.getCategory());
        course.setType(courseDto.getType());
        course.setLevel(courseDto.getLevel());
        course.setDurationHours(courseDto.getDurationHours());
        course.setAddress(courseDto.getAddress());
        course.setRoom(courseDto.getRoom());
        course.setCapacity(courseDto.getCapacity());
        course.setOnlineLink(courseDto.getOnlineLink());
        course.setConnectionInstructions(courseDto.getConnectionInstructions());
        course.setCost(courseDto.getCost());
        course.setFullPayment(courseDto.getFullPayment());
        course.setInstallmentPayment(courseDto.getInstallmentPayment());
        course.setFundingAvailable(courseDto.getFundingAvailable());

        if (courseDto.getInstructor() != null) {
            User instructor = userRepository.findById(courseDto.getInstructor().getId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));
            course.setInstructor(instructor);
        }

        Course savedCourse = courseRepository.save(course);
        return mapToDto(savedCourse);
    }

    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setCoverImage(courseDto.getCoverImage());
        course.setStartDate(courseDto.getStartDate());
        course.setEndDate(courseDto.getEndDate());
        course.setCategory(courseDto.getCategory());
        course.setType(courseDto.getType());
        course.setLevel(courseDto.getLevel());
        course.setDurationHours(courseDto.getDurationHours());
        course.setAddress(courseDto.getAddress());
        course.setRoom(courseDto.getRoom());
        course.setCapacity(courseDto.getCapacity());
        course.setOnlineLink(courseDto.getOnlineLink());
        course.setConnectionInstructions(courseDto.getConnectionInstructions());
        course.setCost(courseDto.getCost());
        course.setFullPayment(courseDto.getFullPayment());
        course.setInstallmentPayment(courseDto.getInstallmentPayment());
        course.setFundingAvailable(courseDto.getFundingAvailable());

        if (courseDto.getInstructor() != null) {
            User instructor = userRepository.findById(courseDto.getInstructor().getId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));
            course.setInstructor(instructor);
        }

        Course updatedCourse = courseRepository.save(course);
        return mapToDto(updatedCourse);
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(id);
    }

    public void enrollStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.getCourses().add(course);
        userRepository.save(student);
    }

    public void unenrollStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.getCourses().remove(course);
        userRepository.save(student);
    }

    private CourseDto mapToDto(Course course) {
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setCoverImage(course.getCoverImage());
        dto.setStartDate(course.getStartDate());
        dto.setEndDate(course.getEndDate());
        dto.setCategory(course.getCategory());
        dto.setType(course.getType());
        dto.setLevel(course.getLevel());
        dto.setDurationHours(course.getDurationHours());
        dto.setAddress(course.getAddress());
        dto.setRoom(course.getRoom());
        dto.setCapacity(course.getCapacity());
        dto.setOnlineLink(course.getOnlineLink());
        dto.setConnectionInstructions(course.getConnectionInstructions());
        dto.setCost(course.getCost());
        dto.setFullPayment(course.getFullPayment());
        dto.setInstallmentPayment(course.getInstallmentPayment());
        dto.setFundingAvailable(course.getFundingAvailable());

        if (course.getInstructor() != null) {
            UserDto instructorDto = new UserDto();
            instructorDto.setId(course.getInstructor().getId());
            instructorDto.setEmail(course.getInstructor().getEmail());
            instructorDto.setFirstName(course.getInstructor().getFirstName());
            instructorDto.setLastName(course.getInstructor().getLastName());
            instructorDto.setProfileImage(course.getInstructor().getProfileImage());
            instructorDto.setRole(course.getInstructor().getRole());

            dto.setInstructor(instructorDto);
        }

        dto.setStudentCount(course.getStudents().size());
        dto.setModuleCount(course.getModules().size());

        return dto;
    }
}