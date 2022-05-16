cmake_minimum_required(VERSION 3.18.1)

project("lib")


set(lib_DIR ${CMAKE_SOURCE_DIR}/libs)


add_library( lib SHARED src/main/cpp/native-lib.cpp)


INCLUDE_DIRECTORIES(src/main/cpp)


add_library(libiiauapp SHARED IMPORTED)
set_target_properties(libiiauapp PROPERTIES IMPORTED_LOCATION ${lib_DIR}/${ANDROID_ABI}/libiiauapp.so)


find_library( log-lib log)


target_link_libraries( lib libiiauapp ${log-lib})