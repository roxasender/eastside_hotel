package com.roxas.eastsidehotel.repository;

import com.roxas.eastsidehotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("select distinct r.roomType from Room r")
    List<String> findDistinctRoomTypes();

    @Query("select r from Room r " +
            "where r.roomType like %:roomType% " +
            "and r.id not in (" +
            " select br.room.id from BookedRoom br " +
            " where ((br.checkInDate <= :checkOutDate) and (br.checkOutDate >= :checkInDate))" +
            ")")
    List<Room> findAvailableRoomsByDatesAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String RoomType);
}
