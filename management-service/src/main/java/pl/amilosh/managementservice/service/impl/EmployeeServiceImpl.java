package pl.amilosh.managementservice.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.amilosh.managementservice.dto.EmployeeDto;
import pl.amilosh.managementservice.dto.request.EmployeeSearchRequest;
import pl.amilosh.managementservice.dto.request.PageableRequest;
import pl.amilosh.managementservice.event.publisher.EmployeeEventPublisher;
import pl.amilosh.managementservice.event.publisher.GenericEventPublisher;
import pl.amilosh.managementservice.exception.ResourceNotFoundException;
import pl.amilosh.managementservice.mapper.EmployeeMapper;
import pl.amilosh.managementservice.model.Employee;
import pl.amilosh.managementservice.repository.EmployeeRepository;
import pl.amilosh.managementservice.service.EmployeeService;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import static pl.amilosh.managementservice.repository.specification.EmployeeSpecification.searchByFieldsAnd;
import static pl.amilosh.managementservice.repository.specification.EmployeeSpecification.searchByFieldsOr;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    @Value("${aws.s3.bucketName}")
    private String awsBucketName;

    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;
    private final EmployeeEventPublisher employeeEventPublisher;
    private final GenericEventPublisher genericEventPublisher;
    private final AmazonS3 amazonS3;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        log.info("Create Employee {}", employeeDto.getEmail());
        var employee = employeeMapper.toEntity(employeeDto);
        persist(employee);
        return toDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        var employees = employeeRepository.findAll();
        return employeeMapper.toDtos(employees);
    }

    @Override
    public Page<EmployeeDto> getAllEmployeesPages(PageableRequest pageableRequest) {
        var pageable = pageableRequest.getPageRequest();
        var employees = employeeRepository.findAll(pageable);
        return employees.map(employeeMapper::toDto);
    }

    @Override
    public Page<EmployeeDto> getAllEmployeesPagesCriteriaBuilder(EmployeeSearchRequest employeeSearchRequest) {
        var employees = employeeRepository.getAllEmployeesPagesCriteriaBuilder(employeeSearchRequest);
        return employees.map(employeeMapper::toDto);
    }

    @Override
    public Page<EmployeeDto> getAllEmployeesPagesSpecificationAnd(EmployeeSearchRequest employeeSearchRequest) {
        var employeeSpecification = searchByFieldsAnd(employeeSearchRequest);
        var pageable = employeeSearchRequest.getPageRequest();
        var employees = employeeRepository.findAll(employeeSpecification, pageable);
        return employees.map(employeeMapper::toDto);
    }

    @Override
    public Page<EmployeeDto> getAllEmployeesPagesSpecificationOr(EmployeeSearchRequest employeeSearchRequest) {
        var employeeSpecification = searchByFieldsOr(employeeSearchRequest);
        var pageable = employeeSearchRequest.getPageRequest();
        var employees = employeeRepository.findAll(employeeSpecification, pageable);
        return employees.map(employeeMapper::toDto);
    }

    @Override
    public EmployeeDto getEmployeeById(Integer id) {
        log.info("Get Employee {}", id);
        var employee = getById(id);
        return toDto(employee);
    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
        log.info("Update Employee {} {}", employeeDto.getId(), employeeDto.getEmail());
        var employee = getById(employeeDto.getId());
        employeeMapper.updateEntity(employeeDto, employee);
        persist(employee);
        return toDto(employee);
    }

    @Override
    public void persist(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public Employee getById(Integer id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee %s not found", id));
    }

    @Override
    public EmployeeDto toDto(Employee employee) {
        return employeeMapper.toDto(employee);
    }

    @Override
    public String publishEmployeeEvent(String email) {
        employeeEventPublisher.publishEmployeeEvent(email);
        genericEventPublisher.publishGenericEvent(email, false);
        genericEventPublisher.publishGenericEvent(email, true);
        log.info("EmployeeServiceImpl thread: {}" + Thread.currentThread().getName());
        return email;
    }

    @Override
    public void aws() throws Exception {
        awsGetAllBuckets();
        awsUploadFileToBucket("fileName.txt", "Content!");
        awsDownloadFileFromBucket("fileName.txt");
    }

    private void awsGetAllBuckets() {
        var buckets = amazonS3.listBuckets();
    }

    private void awsUploadFileToBucket(String fileName, String content) throws Exception {
        var file = createTempFile(content);
        uploadFile(file, fileName);
    }

    private File createTempFile(String content) throws Exception {
        var file = File.createTempFile("temp", ".txt");
        var writer = new PrintWriter(file);
        writer.println(content);
        writer.close();
        return file;
    }

    private void uploadFile(File file, String fileName) {
        var key = "path/to/" + fileName;

        var request = new PutObjectRequest(awsBucketName, key, file);
        var metadata = new ObjectMetadata();
        metadata.setContentType("text/plain");
        request.setMetadata(metadata);

        var result = amazonS3.putObject(request);
        log.info("File uploaded to bucket, tag {}", result.getETag());
    }

    private void awsDownloadFileFromBucket(String fileName) throws Exception {
        var key = "path/to/" + fileName;
        S3Object s3object = amazonS3.getObject(awsBucketName, key);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            log.info(line);
        }
        reader.close();
    }
}
