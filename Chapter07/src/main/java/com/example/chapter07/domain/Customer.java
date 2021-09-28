package com.example.chapter07.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * customerReader : FlatFileItemReader
 : customReader 는 FlatFileITemReader 의 인스턴스다. FlatFileItemReader는 컴포넌트 두개로 구성된다.
 : 하나는 읽어들일 리소스, 다른 하나는 파일의 각 줄을 매칭하는 방법을 정의하는 LineMapper 구현체이다.
 : LineMapper 구현체로는 스프링 배치가 제공하는 DefaultLineMapper 를 사용한다. 각 줄을 파싱해서 FieldSet 을 생성한다.
 : DefaultLineMapper 가 2단계 매핑 작업을 하려면 의존성 2개가 필요하다.
 : 1) LineTokenizer (문자열 파싱 후 FieldSet 으로 매핑)
 * outputWriter : FlatFileItemWriter
 * copyStep : 잡의 스텝을 정의
 * copyJob : 잡을 정의
 */
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    private Long id;

    @Column(name = "firstName")
    private String firstName;
    @Column(name = "middleInitial")
    private String middleInitial;
    @Column(name = "lastName")
    private String lastName;
    //	private String addressNumber;
//	private String street;
    private String address;
    private String city;
    private String state;
    private String zipCode;

//	private List<Transaction> transactions;

    public Customer() {
    }

//	public Customer(String firstName, String middleName, String lastName, String addressNumber, String street, String city, String state, String zipCode) {
//		this.firstName = firstName;
//		this.middleName = middleName;
//		this.lastName = lastName;
//		this.addressNumber = addressNumber;
//		this.street = street;
//		this.city = city;
//		this.state = state;
//		this.zipCode = zipCode;
//	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

//	public String getAddressNumber() {
//		return addressNumber;
//	}
//
//	public void setAddressNumber(String addressNumber) {
//		this.addressNumber = addressNumber;
//	}
//
//	public String getStreet() {
//		return street;
//	}
//
//	public void setStreet(String street) {
//		this.street = street;
//	}


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

//	public List<Transaction> getTransactions() {
//		return transactions;
//	}
//
//	@XmlElementWrapper(name = "transactions")
//	@XmlElement(name = "transaction")
//	public void setTransactions(List<Transaction> transactions) {
//		this.transactions = transactions;
//	}

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", middleInitial='" + middleInitial + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }

//	@Override
//	public String toString() {
//		StringBuilder output = new StringBuilder();
//
//		output.append(firstName);
//		output.append(" ");
//		output.append(middleInitial);
//		output.append(". ");
//		output.append(lastName);
//
//		if(transactions != null&& transactions.size() > 0) {
//			output.append(" has ");
//			output.append(transactions.size());
//			output.append(" transactions.");
//		} else {
//			output.append(" has no transactions.");
//		}
//
//		return output.toString();
//	}

    //	@Override
//	public String toString() {
//		return "Customer{" +
//				"firstName='" + firstName + '\'' +
//				", middleInitial='" + middleInitial + '\'' +
//				", lastName='" + lastName + '\'' +
//				", address='" + address + '\'' +
////				", addressNumber='" + addressNumber + '\'' +
////				", street='" + street + '\'' +
//				", city='" + city + '\'' +
//				", state='" + state + '\'' +
//				", zipCode='" + zipCode + '\'' +
//				'}';
//	}
}