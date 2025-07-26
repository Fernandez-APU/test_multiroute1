package com.example.test1_multiroute

import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object ExcelUtils {

    data class ParcelInfo(
        val sku: String,
        val owner: String,
        val address: String,
        val postalCode: String,
        val size: Int
    )

    // Read Excel file into ParcelInfo list
    fun readParcelData(filePath: String): List<ParcelInfo> {
        val parcelList = mutableListOf<ParcelInfo>()
        val workbook = WorkbookFactory.create(File(filePath))
        val sheet = workbook.getSheetAt(0)

        sheet.drop(1).forEach { row ->
            val sku = row.getCell(0).stringCellValue
            val owner = row.getCell(1).stringCellValue
            val address = row.getCell(2).stringCellValue
            val postalCode = row.getCell(3).stringCellValue
            val size = row.getCell(4).numericCellValue.toInt()

            parcelList.add(ParcelInfo(sku, owner, address, postalCode, size))
        }

        workbook.close()
        return parcelList
    }

    // Group parcels clearly by postal code
    fun groupByPostalCode(parcels: List<ParcelInfo>): Map<String, List<ParcelInfo>> {
        return parcels.groupBy { it.postalCode }
    }

    // Write grouped parcels into new Excel file with clear naming
    fun writeGroupedData(
        groupedData: Map<String, List<ParcelInfo>>,
        serviceName: String,
        garageAddress: String
    ) {
        val workbook = XSSFWorkbook()
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd_hh-mm-a", Locale.ENGLISH)
        val timestamp = dateFormatter.format(Date())

        groupedData.forEach { (postalCode, parcels) ->
            val sheet = workbook.createSheet("Postal_$postalCode")
            val header = sheet.createRow(0)
            header.createCell(0).setCellValue("SKU")
            header.createCell(1).setCellValue("Owner")
            header.createCell(2).setCellValue("Address")
            header.createCell(3).setCellValue("Postal Code")
            header.createCell(4).setCellValue("Size")

            parcels.forEachIndexed { index, parcel ->
                val row = sheet.createRow(index + 1)
                row.createCell(0).setCellValue(parcel.sku)
                row.createCell(1).setCellValue(parcel.owner)
                row.createCell(2).setCellValue(parcel.address)
                row.createCell(3).setCellValue(parcel.postalCode)
                row.createCell(4).setCellValue(parcel.size.toDouble())
            }
        }

        val outputFilename = "${serviceName}_${garageAddress}_$timestamp.xlsx"
        val outputFile = File(outputFilename)
        workbook.write(FileOutputStream(outputFile))
        workbook.close()
    }
}
