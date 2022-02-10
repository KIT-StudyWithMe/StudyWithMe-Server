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

/**
 * Study group report
 *
 * @property studyGroupReportID
 * @property reporterID
 * @property groupID
 * @property groupField
 * @constructor Create empty Study group report
 */
@Entity
data class StudyGroupReport (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val studyGroupReportID: Long,

    val reporterID: Long = 0,

    val groupID: Long = 0,

    val groupField: StudyGroupField
)

//class GroupReportKey constructor () : Serializable (
//    val reporterID: Long,
//    val groupID: Long
//)