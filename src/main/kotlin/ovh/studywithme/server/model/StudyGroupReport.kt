package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.FetchType
import javax.persistence.IdClass
import javax.validation.constraints.NotBlank
import org.hibernate.mapping.ManyToOne

@Entity 
//@IdClass(GroupReportKey.class)
data class StudyGroupReport (
    @Id
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name =  "userID")
    val reporterID: Long,

    //@Id //currently not working
    val groupID: Long,

    //@Id //currently not working
    val groupField: StudyGroupField
)

//class GroupReportKey constructor () : Serializable (
//    val reporterID: Long,
//    val groupID: Long
//)