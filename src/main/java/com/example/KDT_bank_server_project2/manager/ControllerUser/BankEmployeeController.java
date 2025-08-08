package com.example.KDT_bank_server_project2.manager.ControllerUser;





import com.example.KDT_bank_server_project2.manager.DtoUser.ApiResponseUser;
import com.example.KDT_bank_server_project2.manager.DtoUser.EmployeeCreateRequestDto;
import com.example.KDT_bank_server_project2.manager.DtoUser.EmployeeResponseDto;
import com.example.KDT_bank_server_project2.manager.EntityUser.BankEmployee;
import com.example.KDT_bank_server_project2.manager.ServiceUser.BankEmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class BankEmployeeController {

    private final BankEmployeeService bankEmployeeService;

    public BankEmployeeController(BankEmployeeService bankEmployeeService) {
        this.bankEmployeeService = bankEmployeeService;
    }

    // 직원 생성
    @PostMapping
    public ResponseEntity<ApiResponseUser<EmployeeResponseDto>> createEmployee(@Valid @RequestBody EmployeeCreateRequestDto requestDto) {
        try {
            BankEmployee employee = convertToEntity(requestDto);
            BankEmployee createdEmployee = bankEmployeeService.createEmployee(employee);
            EmployeeResponseDto responseDto = new EmployeeResponseDto(createdEmployee);

            return ResponseEntity.ok(ApiResponseUser.success("직원이 성공적으로 생성되었습니다.", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
        }
    }

    // 모든 직원 조회
    @GetMapping
    public ResponseEntity<ApiResponseUser<List<EmployeeResponseDto>>> getAllEmployees() {
        List<BankEmployee> employees = bankEmployeeService.getAllEmployees();
        List<EmployeeResponseDto> responseDtos = employees.stream()
                .map(EmployeeResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }

    // ID로 직원 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseUser<EmployeeResponseDto>> getEmployeeById(@PathVariable String id) {
        Optional<BankEmployee> employee = bankEmployeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            EmployeeResponseDto responseDto = new EmployeeResponseDto(employee.get());
            return ResponseEntity.ok(ApiResponseUser.success(responseDto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 이메일로 직원 조회
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponseUser<EmployeeResponseDto>> getEmployeeByEmail(@PathVariable String email) {
        Optional<BankEmployee> employee = bankEmployeeService.getEmployeeByEmail(email);
        if (employee.isPresent()) {
            EmployeeResponseDto responseDto = new EmployeeResponseDto(employee.get());
            return ResponseEntity.ok(ApiResponseUser.success(responseDto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 직원 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseUser<EmployeeResponseDto>> updateEmployee(@PathVariable String id, @Valid @RequestBody EmployeeCreateRequestDto requestDto) {
        try {
            BankEmployee updateData = convertToEntity(requestDto);
            BankEmployee updatedEmployee = bankEmployeeService.updateEmployee(id, updateData);
            EmployeeResponseDto responseDto = new EmployeeResponseDto(updatedEmployee);

            return ResponseEntity.ok(ApiResponseUser.success("직원 정보가 수정되었습니다.", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
        }
    }

    // 직원 상태 변경
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponseUser<EmployeeResponseDto>> updateEmployeeStatus(@PathVariable String id,
                                                                                    @RequestParam BankEmployee.EmployeeStatus status) {
        try {
            BankEmployee updatedEmployee = bankEmployeeService.updateEmployeeStatus(id, status);
            EmployeeResponseDto responseDto = new EmployeeResponseDto(updatedEmployee);
            return ResponseEntity.ok(ApiResponseUser.success("직원 상태가 변경되었습니다.", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
        }
    }

    // 부서별 직원 조회
    @GetMapping("/department/{department}")
    public ResponseEntity<ApiResponseUser<List<EmployeeResponseDto>>> getEmployeesByDepartment(@PathVariable String department) {
        List<BankEmployee> employees = bankEmployeeService.getEmployeesByDepartment(department);
        List<EmployeeResponseDto> responseDtos = employees.stream()
                .map(EmployeeResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }

    // 역할별 직원 조회
    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponseUser<List<EmployeeResponseDto>>> getEmployeesByRole(@PathVariable BankEmployee.EmployeeRole role) {
        List<BankEmployee> employees = bankEmployeeService.getEmployeesByRole(role);
        List<EmployeeResponseDto> responseDtos = employees.stream()
                .map(EmployeeResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }

    // 활성 직원 조회
    @GetMapping("/active")
    public ResponseEntity<ApiResponseUser<List<EmployeeResponseDto>>> getActiveEmployees() {
        List<BankEmployee> employees = bankEmployeeService.getActiveEmployees();
        List<EmployeeResponseDto> responseDtos = employees.stream()
                .map(EmployeeResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }

    // 직원 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponseUser<List<EmployeeResponseDto>>> searchEmployees(@RequestParam String keyword) {
        List<BankEmployee> employees = bankEmployeeService.searchEmployees(keyword);
        List<EmployeeResponseDto> responseDtos = employees.stream()
                .map(EmployeeResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }

    // 비밀번호 변경
    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponseUser<Void>> changePassword(@PathVariable String id, @RequestParam String newPassword) {
        try {
            bankEmployeeService.changePassword(id, newPassword);
            return ResponseEntity.ok(ApiResponseUser.success("비밀번호가 변경되었습니다.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
        }
    }

    // DTO -> Entity 변환
    private BankEmployee convertToEntity(EmployeeCreateRequestDto dto) {
        BankEmployee employee = new BankEmployee();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPassword(dto.getPassword());
        employee.setDepartment(dto.getDepartment());
        employee.setRole(BankEmployee.EmployeeRole.valueOf(dto.getRole()));
        employee.setPhone(dto.getPhone());
        return employee;
    }
}
