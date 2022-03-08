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
 * Contains all relevant data about a user's report regarding a study-group.
 *
 * @property studyGroupReportID The group-report's unique identifier, which is auto-generated.
 * @property reporterID The reporting user's unique identifier.
 * @property groupID The study-group's unique identifier.
 * @property groupField The group's freetext field which was reported.
 * @constructor Create a new StudyGroupReport.
 */
@Entity
data class StudyGroupReport (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val studyGroupReportID: GroupReportID,

    val reporterID: UserID,

    val groupID: GroupID,

    val groupField: StudyGroupField
)

/**
 * GroupReportID
 *
 * @property groupReportID
 * @constructor Create empty Group i d
 */
@JvmInline
value class GroupReportID(private val groupReportID: Long)