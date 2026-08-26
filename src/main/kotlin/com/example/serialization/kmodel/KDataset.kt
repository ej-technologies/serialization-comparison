package com.example.serialization.kmodel

import com.example.serialization.DatasetGenerator
import com.example.serialization.model.Address
import com.example.serialization.model.Category
import com.example.serialization.model.Customer
import com.example.serialization.model.Order
import com.example.serialization.model.OrderLine
import com.example.serialization.model.Product

object KDataset {
    fun create(): List<KOrder> = DatasetGenerator.create().map { it.toK() }

    private fun Order.toK() = KOrder(
        id,
        customer.toK(),
        lines.map { it.toK() },
        KOrderStatus.valueOf(status.name),
        created,
        shippingAddress.toK(),
        LinkedHashMap(attributes),
        notes
    )

    private fun Customer.toK() = KCustomer(id, firstName, lastName, email, dateOfBirth, isVip, addresses.map { it.toK() })

    private fun Address.toK() = KAddress(street, city, zip, country)

    private fun Category.toK(): KCategory = KCategory(id, name, parent?.toK())

    private fun Product.toK() = KProduct(sku, name, description, category.toK(), price, weightGrams, LinkedHashSet(tags), thumbnail)

    private fun OrderLine.toK() = KOrderLine(product.toK(), quantity, unitPrice, discount)
}
