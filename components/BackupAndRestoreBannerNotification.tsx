import React from 'react';
import {useBackupScreen} from '../screens/backupAndRestore/BackupController';
import {BannerNotification} from './BannerNotification';
import {useTranslation} from 'react-i18next';
import {useBackupRestoreScreen} from '../screens/Settings/BackupRestoreController';
import {BannerStatusType} from './BannerNotification';

export const BackupAndRestoreBannerNotification: React.FC = () => {
  const backUpController = useBackupScreen();
  const restoreController = useBackupRestoreScreen();

  const {t} = useTranslation('BackupAndRestoreBanner');

  function backupFailure() {
    const translation = t(
      `backupFailure.${backUpController.backupErrorReason}`,
    );

    return (
      <BannerNotification
        type={BannerStatusType.ERROR}
        message={translation}
        onClosePress={backUpController.DISMISS}
        key={`backupFailure-${backUpController.backupErrorReason}Popup`}
        testId={`backupFailure-${backUpController.backupErrorReason}Popup`}
      />
    );
  }

  function restoreFailure() {
    const translation = t(
      `restoreFailure.${restoreController.restoreErrorReason}`,
    );

    return (
      <BannerNotification
        type={BannerStatusType.ERROR}
        key={`restoreFailure-${restoreController.restoreErrorReason}Popup`}
        message={translation}
        onClosePress={restoreController.DISMISS}
        testId={`restoreFailure-${restoreController.restoreErrorReason}Popup`}
      />
    );
  }

  return (
    <>
      {backUpController.showBackupInProgress && (
        <BannerNotification
          type={BannerStatusType.IN_PROGRESS}
          message={t('backupInProgress')}
          onClosePress={backUpController.DISMISS_SHOW_BACKUP_IN_PROGRESS}
          key={'dataBackupInProgress'}
          testId={'dataBackupInProgress'}
        />
      )}

      {backUpController.isBackingUpSuccess && (
        <BannerNotification
          type={BannerStatusType.SUCCESS}
          message={t('backupSuccessful')}
          onClosePress={backUpController.DISMISS}
          key={'dataBackupSuccessPopup'}
          testId={'dataBackupSuccessPopup'}
        />
      )}

      {backUpController.isBackingUpFailure && backupFailure()}

      {restoreController.showRestoreInProgress && (
        <BannerNotification
          type={BannerStatusType.IN_PROGRESS}
          message={t('restoreInProgress')}
          onClosePress={restoreController.DISMISS_SHOW_RESTORE_IN_PROGRESS}
          key={'restoreInProgress'}
          testId={'restoreInProgress'}
        />
      )}

      {restoreController.isBackUpRestoreSuccess && (
        <BannerNotification
          type={BannerStatusType.SUCCESS}
          message={t('restoreSuccessful')}
          onClosePress={restoreController.DISMISS}
          key={'restoreBackupSuccessPopup'}
          testId={'restoreBackupSuccessPopup'}
        />
      )}
      {restoreController.isBackUpRestoreFailure && restoreFailure()}
    </>
  );
};
